package com.iliani14.pg6100.util;

import com.iliani14.pg6100.entity.Question;
import com.iliani14.pg6100.entity.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@Stateless
public class DeleterEJB {

    @PersistenceContext
    private EntityManager em;

    public void deleteEntities(Class<?> entity){

        if(entity == null || entity.getAnnotation(Entity.class) == null){
            throw new IllegalArgumentException("Invalid non-entity class");
        }

        String name = entity.getSimpleName();

        /*
            Note: we passed as input a Class<?> instead of a String to
            avoid SQL injection. However, being here just test code, it should
            not be a problem. But, as a good habit, always be paranoiac about
            security, above all when you have code that can delete the whole
            database...
         */

        Query query = em.createQuery("delete from " + name);
        query.executeUpdate();
    }


    public void deleteQuestions(){
        List<Question> questions = em.createNamedQuery(Question.GET_ALL_QUESTIONS).getResultList();
        for(Question q: questions){
            SubSubCategory subSubCategory = em.find(SubSubCategory.class, q.getSubSubCategories().getId());
            subSubCategory.getQuestions().remove(q);
        }

    }

}