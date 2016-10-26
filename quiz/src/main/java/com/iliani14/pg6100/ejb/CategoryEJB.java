package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Category;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */
@Stateless
public class CategoryEJB {


    @PersistenceContext
    private EntityManager em;


    public Category createCategory(long id, String name){
        Category c = new Category();
        c.setId(id);
        c.setName(name);

        em.persist(c);

        return c;

    }

    public Category findCategoryById(long id){
        return em.find(Category.class, id);
    }

    public List<Category> getAllCategories(){
        Query query = em.createNamedQuery(Category.GET_ALL_CATEGORIES);
        List<Category> categories = query.getResultList();

        return categories;

    }

    public Category getCategoryByName(String name) {
        Query query = em.createNamedQuery(Category.GET_CATEGORY_BY_NAME);
        query.setParameter("name", name);
        return (Category) query.getSingleResult();
    }

    public void deleteCategory(long id){
        Query query = em.createQuery("DELETE FROM SubCategory c WHERE c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

}
