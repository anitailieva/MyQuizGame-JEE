package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Category;
import com.iliani14.pg6100.entity.SubCategory;

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
public class SubCategoryEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CategoryEJB categoryEJB;

    public Long createSubCategory(long categoryId, String name) {
        Category c = categoryEJB.findCategoryById(categoryId);
        SubCategory sub = new SubCategory();
        sub.setName(name);
        sub.setCategory(c);

        em.persist(sub);

        c.getSubCategories().add(sub);
        em.persist(c);

        return sub.getId();
    }

    public SubCategory findSubCategoryById(long id) {
        return em.find(SubCategory.class, id);
    }

    public List<SubCategory> getAllSubCategories() {
        Query query = em.createNamedQuery(SubCategory.GET_ALL_SUBCATEGORIES);
        List<SubCategory> subCategories = query.getResultList();

        return subCategories;
    }

    public SubCategory getSubCategoryByCategoryName(String categoryName) {
        Query query = em.createNamedQuery(SubCategory.GET_SUBCATEGORY_BY_CATEGORY);
        query.setParameter("categoryName", categoryName);

        return (SubCategory) query.getSingleResult();
    }

    public void deleteSubCategory(long id) {
        SubCategory subCategory = em.find(SubCategory.class, id);
        if (subCategory != null) {
            em.remove(subCategory);
        }
    }

    public void updateSubCategory(long id, String newName) {
        SubCategory subCategory = em.find(SubCategory.class, id);
        if (subCategory != null) {
            subCategory.setName(newName);

        }
    }
}