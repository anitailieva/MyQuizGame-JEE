package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Category;
import com.iliani14.pg6100.entity.Question;
import com.iliani14.pg6100.entity.SubCategory;
import com.iliani14.pg6100.entity.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anitailieva on 26/10/2016.
 */
@Stateless
public class CategoryEJB {


    @PersistenceContext
    private EntityManager em;


    public Long createCategory(String name){
        Category c = new Category();
        c.setName(name);

        em.persist(c);

        return c.getId();

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
        Category category = em.find(Category.class, id);
        if(category != null){
            em.remove(category);
        }
    }

    public void updateCategory(long id, String newName){
        Category category = em.find(Category.class, id);
        if(category!=null)
            {
                category.setName(newName);

        }
    }

    public List<Category> getAllCategoriesWithAtLeastOneQuiz() {
        List<Question> questions = em.createNamedQuery(Question.GET_ALL_QUESTIONS).getResultList();


        return questions.stream().map(Question::getSubSubCategories).collect(Collectors.toList())
                .stream().map(SubSubCategory::getSubCategories).collect(Collectors.toList())
                .stream().map(SubCategory::getCategory).collect(Collectors.toList());
    }

    public List<SubCategory> getAllSubCategoriesForACategory(Long id) {
        return new ArrayList<>(findCategoryById(id).getSubCategories());
    }

    public List<Question> getAllQuizzesForCategory(Long id){
        Category category = em.find(Category.class, id);

        if (category == null) throw new IllegalArgumentException("There is no category with the id: " + id);

        return category.getListOfQuestions();
    }

}
