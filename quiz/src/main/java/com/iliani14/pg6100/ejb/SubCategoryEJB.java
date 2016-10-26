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

    public SubCategory createSubCategory(Category category, String name){
        SubCategory sub = new SubCategory();
        sub.setName(name);

        em.persist(sub);

        Category c = categoryEJB.findCategoryById(category.getId());
        c.getSubCategories().add(sub);

        return sub;
    }

    public SubCategory findSubCategoryById(long id){
        return em.find(SubCategory.class, id);
    }

    public List<SubCategory> getAllSubCategories(){
        Query query = em.createNamedQuery(SubCategory.GET_ALL_SUBCATEGORIES);
        List<SubCategory> subCategories = query.getResultList();

        return subCategories;
    }
    public SubCategory getSubCategoryByCategoryName(String categoryName){
        Query query = em.createNamedQuery(SubCategory.GET_SUBCATEGORY_BY_CATEGORY);
        query.setParameter("categoryName", categoryName);

        return (SubCategory) query.getSingleResult();
    }

    public void deleteSubCategory(long id){
        Query query = em.createQuery("DELETE FROM SubCategory s WHERE s.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
