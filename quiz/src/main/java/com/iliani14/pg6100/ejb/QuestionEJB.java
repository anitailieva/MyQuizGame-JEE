package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Question;
import com.iliani14.pg6100.entity.SubSubCategory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@Stateless
public class QuestionEJB {


    @PersistenceContext
    private EntityManager em;

    @EJB
    private SubSubCategoryEJB subSubCategoryEJB;

    public Question createQuestion(SubSubCategory s, String text, List<String> answers, String theCorrectAnswer) {
        SubSubCategory subsub = subSubCategoryEJB.findSubSubCategoryById(s.getId());
        Question q = new Question();
        q.setText(text);
        q.setAnswers(answers);
        q.setTheCorrectAnswer(theCorrectAnswer);
        q.setSubSubCategories(subsub);

        em.persist(q);


        subsub.getQuestions().add(q);

        em.persist(subsub);

        return q;
    }

    public Question findQuestionById(long id) {
        return em.find(Question.class, id);
    }

    public List<Question> getAllQuestions() {
        Query query = em.createNamedQuery(Question.GET_ALL_QUESTIONS);
        List<Question> questions = query.getResultList();

        return questions;
    }

    public Question getQuizBySubSubCategoryName(String subSubCategoryName) {
        Query query = em.createNamedQuery(Question.GET_QUESTION_BY_SUBSUBCATEGORY);
        query.setParameter("subSubCategoryName", subSubCategoryName);

        return (Question) query.getResultList();
    }


    public void deleteQuestion(long id) {
        Question q = em.find(Question.class, id);
        if (q == null) {
            return;
        }
        SubSubCategory subSubCategory = em.find(SubSubCategory.class, q.getSubSubCategories().getId());
        subSubCategory.getQuestions().remove(q);
        em.persist(subSubCategory);
        em.remove(q);
    }

    public void updateQuestion(long id, String newName) {
        Question question= em.find(Question.class, id);
        if (question != null) {
            question.setText(newName);

        }
    }

}