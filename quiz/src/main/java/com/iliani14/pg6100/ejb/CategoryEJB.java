package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Category;
import com.iliani14.pg6100.entity.Question;
import com.iliani14.pg6100.entity.SubCategory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;


/**
 * Created by anitailieva on 26/10/2016.
 */
@Stateless
public class CategoryEJB {


    @PersistenceContext
    private EntityManager em;

    @EJB
    private QuestionEJB questionEJB;

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
        List<Question> questions = questionEJB.getAllQuestions();

        if(questions.size() == 0) {
            return new ArrayList<>();
        }

        Set<Long> categories = questions
                .stream()
                .map(q -> q.getSubSubCategories().getSubCategories().getCategory().getId())
                .collect(toSet());


        return getAllCategories()
                .stream()
                .filter(c -> categories.contains(c.getId()))
                .collect(Collectors.toList());
    }

    public List<SubCategory> getAllSubCategoriesForACategory(Long id) {
        return new ArrayList<>(findCategoryById(id).getSubCategories());
    }

    public List<Question> getAllQuizzesForCategory(Long id){
        Category category = em.find(Category.class, id);

        if (category == null) throw new IllegalArgumentException("There is no category with the id: " + id);

        return category.getListOfQuestions();
    }

    public List<Long> getRandomQuizzesForCategory(Long catId, int n) {
        List<Question> questions = em.find(Category.class, catId).getListOfQuestions();
        List<Long> id = new ArrayList<>();

        while (id.size() != n && questions.size() != 0) {
            id.add(questions.remove(new Random().nextInt(questions.size())).getId());
        }
        return id;
    }
}
