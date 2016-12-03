package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Category;
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
public class SubCategoryEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CategoryEJB categoryEJB;

    public long createSubCategory(long categoryId, String name) {
        Category c = categoryEJB.findCategoryById(categoryId);
        SubCategory sub = new SubCategory();
        sub.setName(name);
        sub.setCategory(c);

        em.persist(sub);

        c.getSubCategories().add(sub);

        return sub.getId();
    }

    public SubCategory findSubCategoryById(Long id) {
        return em.find(SubCategory.class, id);
    }

    public List<SubCategory> getSubCategoriesList(int limit) {
        return em.createNamedQuery(SubCategory.GET_ALL_SUBCATEGORIES).setMaxResults(limit)
                .getResultList();

    }

    public List<SubCategory> getAllSubCategories() {
        Query query = em.createNamedQuery(SubCategory.GET_ALL_SUBCATEGORIES);
        List<SubCategory> subCategories = query.getResultList();

        return subCategories;
    }

    public List<SubCategory> getSubCategoriesByCategoryId(Long categoryId) {
        Query query = em.createQuery("SELECT s FROM SubCategory s WHERE s.category.id = ?1");
        query.setParameter(1, categoryId);

        return query.getResultList();
    }
    public List<SubCategory> getSubCategoryByCategoryName(String categoryName) {
        Query query = em.createNamedQuery(SubCategory.GET_SUBCATEGORY_BY_CATEGORY);
        query.setParameter(1, categoryName);

        return query.getResultList();
    }
    public SubCategory getSubCategoryByCategoryIdAndOwnId(long categoryId, long id) {
        Query query = em.createQuery("SELECT s FROM SubCategory s WHERE s.category.id = ?1 AND s.id = ?2");
        query.setParameter(1, categoryId);
        query.setParameter(2, id);

        try {
            return (SubCategory) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public boolean deleteSubCategory(long id) {
            SubCategory subCategory = em.find(SubCategory.class, id);
            if (subCategory == null) return false;
            Category category = em.find(Category.class, subCategory.getCategory().getId());
            category.getSubCategories().remove(id);
            return true;
        }

    public boolean updateSubCategory(long id, String newName) {
        SubCategory subCategory = em.find(SubCategory.class, id);
        if (subCategory == null) return false;
            subCategory.setName(newName);
        return true;
    }

    public List<SubSubCategory> getAllSubSubCategoriesForSubCategory(long id, int limit) {
        return em.createQuery("select ssb from SubSubCategory ssb where ssb.subCategories.id = :id")
                .setParameter("id", id)
                .setMaxResults(limit)
                .getResultList();

    }

    public List<Long> getRandomQuizzesForSubCategory(long subId, int n) {
        List<Question> questions = em.find(SubCategory.class, subId).getListOfQuestions();
        List<Long> ids = new ArrayList<>();

        while (ids.size() != n && questions.size() != 0) {
            ids.add(questions.remove(new Random().nextInt(questions.size())).getId());
        }
        return ids;
    }

}