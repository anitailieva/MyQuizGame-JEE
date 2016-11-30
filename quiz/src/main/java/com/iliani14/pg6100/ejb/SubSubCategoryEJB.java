package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Question;
import com.iliani14.pg6100.entity.SubCategory;
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
public class SubSubCategoryEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SubCategoryEJB subCategoryEJB;

    @EJB
    private QuestionEJB questionEJB;

    public Long createSubSubCategory(Long subCategoryId, String name){
        SubCategory sub = subCategoryEJB.findSubCategoryById(subCategoryId);
        SubSubCategory subsub = new SubSubCategory();
        subsub.setName(name);
        subsub.setSubCategories(sub);

        em.persist(subsub);

        sub.getSubSubCategories().add(subsub);

        return subsub.getId();
    }

    public SubSubCategory findSubSubCategoryById(Long id){
        return em.find(SubSubCategory.class, id);

    }


    public List<SubSubCategory> getSubSubCategoriesByCategoryIdAndSubCategoryId(Long categoryId, Long subcategoryId){
        Query query = em.createQuery("SELECT s FROM SubSubCategory s WHERE s.subCategories.category.id = ?1 " +
                "AND s.subCategories.id = ?2");
        query.setParameter(1, categoryId);
        query.setParameter(2, subcategoryId);

        return query.getResultList();

    }
    public SubSubCategory getSubSubCategoryByCategoryIdSubCategoryIdAndId(Long categoryId, Long subcategoryId, Long id){
        Query query = em.createQuery("SELECT s FROM SubSubCategory s WHERE s.subCategories.category.id = ?1 " +
                "AND s.subCategories.id = ?2 AND s.id = ?3");
        query.setParameter(1, categoryId);
        query.setParameter(2, subcategoryId);
        query.setParameter(3, id);

        try {
            return (SubSubCategory) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public List<SubSubCategory> getSubSubCategoryBySubCategoryName(String subCategoryName){
        Query query = em.createNamedQuery(SubSubCategory.GET_SUBSUBCATEGORY_BY_SUBCATEGORY);
        query.setParameter(1, subCategoryName);

        return query.getResultList();
    }

    public boolean deleteSubSubCategory(Long id) {
      SubSubCategory ss = em.find(SubSubCategory.class, id);
        if (ss == null) return false;
        SubCategory subCategory = em.find(SubCategory.class, ss.getSubCategories().getId());
        subCategory.getSubSubCategories().remove(id);
        return true;

    }
    public boolean updateSubSubCategory(Long id, String newName) {
        SubSubCategory subSubCategory = em.find(SubSubCategory.class, id);
        if (subSubCategory == null) return false;
            subSubCategory.setName(newName);

        return true;
    }

    public List<SubSubCategory> getAllSubSubCategories() {
        return em.createNamedQuery(SubSubCategory.GET_ALL_SUBSUBCATEGORIES)
                .getResultList();
    }

    public List<SubSubCategory> getAllSubSubCategoriesWithAtleastOneQuiz(int limit){
        return em.createQuery("select s from SubSubCategory s where s.questions.size > 0")
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Long> getRandomQuizzesForSubSubCategory(Long subsubId, int n) {
        List<Question> questions = em.find(SubSubCategory.class, subsubId).getListOfQuestions();
        List<Long> id = new ArrayList<>();

        while (id.size() != n && questions.size() != 0) {
            id.add(questions.remove(new Random().nextInt(questions.size())).getId());
        }
        return id;
    }
}
