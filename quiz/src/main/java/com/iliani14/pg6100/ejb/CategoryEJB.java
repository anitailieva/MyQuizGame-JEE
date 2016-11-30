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
import java.util.Set;

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

    public long createCategory(String name){
        Category c = new Category();
        c.setName(name);

        em.persist(c);

        return c.getId();

    }

    public Category findCategoryById(long id){
        return em.find(Category.class, id);
    }

    public Category getCategory(long id, boolean expand){
        Category category = findCategoryById(id);
        if(expand){
            category.getSubCategories()
                    .forEach(subCategory -> subCategory.getSubSubCategories().size());
        }
        return category;
    }


    public List<Category> getAllCategories(int limit, boolean expand){

        List<Category> list =
                em.createNamedQuery(Category.GET_ALL_CATEGORIES).setMaxResults(limit).getResultList();

        if(expand) {
            list.forEach(rootCategory -> rootCategory.getSubCategories().size());

            List<SubCategory> subCategories = new ArrayList<>();
            list.forEach(rootCategory -> subCategories.addAll(rootCategory.getSubCategories()));

            subCategories.forEach(subCategory -> subCategory.getSubSubCategories().size());
        }

        return list;


    }

    public Category getCategoryByName(String name) {
        Query query = em.createNamedQuery(Category.GET_CATEGORY_BY_NAME);
        query.setParameter("name", name);
        return (Category) query.getSingleResult();
    }

    public boolean deleteCategory(long id){
        Category category = em.find(Category.class, id);
        if(category == null) return false;
        em.remove(category);
        return true;

    }

    public boolean updateCategory(long id, String newName){
        Category category = em.find(Category.class, id);
        if (category == null) return false;
        category.setName(newName);
        return true;
    }

    public List<Category> getAllCategoriesWithAtLeastOneQuiz(int limit) {
        List<Question> questions =em.createNamedQuery(Question.GET_ALL_QUESTIONS).getResultList();

        Set<Category> categories = questions.stream().map(Question::getSubSubCategories).collect(toSet())
                .stream().map(SubSubCategory::getSubCategories).collect(toSet())
                .stream().map(SubCategory::getCategory).collect(toSet());

        int size = categories.size();

        return new ArrayList<>(categories).subList(0, limit > size ? size : limit);
    }

    public List<SubCategory> getAllSubCategoriesForACategory(long id, int limit) {
        return em.createQuery("select s from SubCategory s where s.category.id = :id")
                .setParameter("id", id)
                .setMaxResults(limit)
                .getResultList();

    }

    public List<Question> getAllQuestionsForCategory(long id) {
        Category category = em.find(Category.class, id);
        if (category == null) throw new IllegalArgumentException("There is no category with id: " + id);

        return category.getListOfQuestions();
    }

    public List<Question> getAllQuestionsForCategory(long id, int max) {
        Category category = em.find(Category.class, id);
        if (category == null) throw new IllegalArgumentException("There is no category with id: " + id);

        int sizeOfList = category.getListOfQuestions().size();

        return category.getListOfQuestions().subList(0, max > sizeOfList ? sizeOfList : max);
    }

    public List<Long> getRandomQuizzesForCategory(long catId, int n) {
        List<Question> questions = em.find(Category.class, catId).getListOfQuestions();
        List<Long> id = new ArrayList<>();

        while (id.size() != n && questions.size() != 0) {
            id.add(questions.remove(new Random().nextInt(questions.size())).getId());
        }
        return id;
    }
}
