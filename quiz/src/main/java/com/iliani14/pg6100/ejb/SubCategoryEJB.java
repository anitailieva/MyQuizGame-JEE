package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Category;
import com.iliani14.pg6100.entity.SubCategory;
import com.iliani14.pg6100.entity.SubSubCategory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@Stateless
public class SubCategoryEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CategoryEJB categoryEJB;

    public Long createSubCategory(Long categoryId, String name) {
        Category c = categoryEJB.findCategoryById(categoryId);
        SubCategory sub = new SubCategory();
        sub.setName(name);
        sub.setCategory(c);

        em.persist(sub);

        c.getSubCategories().add(sub);
        em.persist(c);

        return sub.getId();
    }

    public SubCategory findSubCategoryById(Long id) {
        return em.find(SubCategory.class, id);
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
    public SubCategory getSubCategoryByCategoryIdAndOwnId(Long categoryId, Long id) {
        Query query = em.createQuery("SELECT s FROM SubCategory s WHERE s.category.id = ?1 AND s.id = ?2");
        query.setParameter(1, categoryId);
        query.setParameter(2, id);

        try {
            return (SubCategory) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public void deleteSubCategory(Long id) {
        SubCategory subCategory = em.find(SubCategory.class, id);
        if (subCategory != null) {
            em.remove(subCategory);
        }
    }

    public void updateSubCategory(Long id, String newName) {
        SubCategory subCategory = em.find(SubCategory.class, id);
        if (subCategory != null) {
            subCategory.setName(newName);

        }
    }

    public List<SubSubCategory> getAllSubSubCategoriesForSubCategory(Long id) {
        return new ArrayList<>(findSubCategoryById(id).getSubSubCategories());
    }

}