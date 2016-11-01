package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.SubCategory;
import com.iliani14.pg6100.entity.SubSubCategory;

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
public class SubSubCategoryEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SubCategoryEJB subCategoryEJB;


    public Long createSubSubCategory(Long subCategoryId, String name){
        SubCategory sub = subCategoryEJB.findSubCategoryById(subCategoryId);
        SubSubCategory subsub = new SubSubCategory();
        subsub.setName(name);
        subsub.setSubCategories(sub);

        em.persist(subsub);

        sub.getSubSubCategories().add(subsub);
        em.persist(sub);


        return subsub.getId();
    }

    public SubSubCategory findSubSubCategoryById(Long id){
        return em.find(SubSubCategory.class, id);

    }

    public List<SubSubCategory> getAllSubSubCategories(){
        Query query = em.createNamedQuery(SubSubCategory.GET_ALL_SUBSUBCATEGORIES);
        List<SubSubCategory> subSubCategories = query.getResultList();

        return subSubCategories;
    }

    public SubSubCategory getSubSubCategoryBySubCategoryName(String subCategoryName){
        Query query = em.createNamedQuery(SubSubCategory.GET_SUBSUBCATEGORY_BY_SUBCATEGORY);
        query.setParameter("subCategoryName", subCategoryName);

        return (SubSubCategory) query.getResultList();
    }

    public void deleteSubSubCategory(Long id){
        SubSubCategory subSubCategory = em.find(SubSubCategory.class, id);
        if(subSubCategory != null){
            em.remove(subSubCategory);
        }
    }
    public void updateSubSubCategory(Long id, String newName) {
        SubSubCategory subSubCategory = em.find(SubSubCategory.class, id);
        if (subSubCategory != null) {
            subSubCategory.setName(newName);

        }
    }
}
