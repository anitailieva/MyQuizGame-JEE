package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Quiz;
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
public class QuizEJB {


    @PersistenceContext
    private EntityManager em;

    @EJB
    private SubSubCategoryEJB subSubCategoryEJB;

    public Quiz createQuiz(SubSubCategory s, String question, List<String> answers, String theCorrectAnswer){
        Quiz q = new Quiz();
        q.setQuestion(question);
        q.setAnswers(answers);
        q.setTheCorrectAnswer(theCorrectAnswer);

        em.persist(q);

        SubSubCategory subsub = subSubCategoryEJB.findSubSubCategoryById(s.getId());
        subsub.getQuizes().add(q);


        return  q;
    }

    public Quiz findQuizById(long id){
        return em.find(Quiz.class, id);
    }

    public List<Quiz> getAllQuizes(){
        Query query = em.createNamedQuery(Quiz.GET_ALL_QUIZES);
        List<Quiz> quizes = query.getResultList();

        return quizes;
    }

    public Quiz getQuizBySubSubCategoryName(String subSubCategoryName){
        Query query = em.createNamedQuery(Quiz.GET_QUIZ_BY_SUBSUBCATEGORY);
        query.setParameter("subSubCategoryName", subSubCategoryName);

        return (Quiz) query.getResultList();
    }

    public void deleteQuiz(long id){
        Quiz quiz = em.find(Quiz.class, id);
        if(quiz != null){
            em.remove(quiz);
        }
    }
}
