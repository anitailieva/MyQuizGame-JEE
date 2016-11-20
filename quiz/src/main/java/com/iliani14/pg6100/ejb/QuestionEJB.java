package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Question;
import com.iliani14.pg6100.entity.SubSubCategory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by anitailieva on 26/10/2016.
 */

@Stateless
public class QuestionEJB {


    @PersistenceContext
    private EntityManager em;

    @EJB
    private SubSubCategoryEJB subSubCategoryEJB;

    public Long createQuestion(Long subsubId, String question, List<String> answers, String theCorrectAnswer) {

        SubSubCategory subsub = subSubCategoryEJB.findSubSubCategoryById(subsubId);
        Question q = new Question();
        q.setQuestion(question);
        q.setAnswers(answers);
        q.setTheCorrectAnswer(theCorrectAnswer);
        q.setSubSubCategories(subsub);

        subsub.getQuestions().add(q);
        em.persist(q);

        return q.getId();
    }

    public Question findQuestionById(Long id) {
        return em.find(Question.class, id);
    }

    public List<Question> getAllQuestions() {
        Query query = em.createNamedQuery(Question.GET_ALL_QUESTIONS);
        List<Question> questions = query.getResultList();

        return questions;
    }

    public List<Question> getQuestionBySubSubCategoryName(String subSubCategoryName) {
        Query query = em.createNamedQuery(Question.GET_QUESTION_BY_SUBSUBCATEGORY);
        query.setParameter(1, subSubCategoryName);

        return query.getResultList();
    }


    public void deleteQuestion(Long id) {
        Question q = em.find(Question.class, id);
        if (q == null) {
            return;
        }
        SubSubCategory subSubCategory = em.find(SubSubCategory.class, q.getSubSubCategories().getId());
        subSubCategory.getQuestions().remove(q);
        em.persist(subSubCategory);
        em.remove(q);
    }

    public void updateQuestion(Long id, String newName) {
        Question question= em.find(Question.class, id);
        if (question != null) {
            question.setQuestion(newName);

        }
    }

    public List<Long> getRandomQuizzes(int n) {
        List<Question> questions = getAllQuestions();
        List<Long> id = new ArrayList<>();

        while (id.size() != n && questions.size() != 0) {
            id.add(questions.remove(new Random().nextInt(questions.size())).getId());
        }
        return id;
    }

}