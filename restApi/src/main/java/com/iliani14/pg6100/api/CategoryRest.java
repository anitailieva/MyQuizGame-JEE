package com.iliani14.pg6100.api;

import com.google.common.base.Throwables;
import com.iliani14.pg6100.dto.*;
import com.iliani14.pg6100.ejb.CategoryEJB;
import com.iliani14.pg6100.ejb.SubCategoryEJB;
import com.iliani14.pg6100.ejb.SubSubCategoryEJB;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Created by anitailieva on 27/10/2016.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class CategoryRest implements CategoryRestApi {

    @EJB
    private CategoryEJB categoryEJB;

    @EJB
    private SubCategoryEJB subCategoryEJB;

    @EJB
    private SubSubCategoryEJB subSubCategoryEJB;

    @Override
    public Long createCategory(CategoryDto dto) {
        if(dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly generated category", 400);
        }
        Long c;
        try{
            c = categoryEJB.createCategory(dto.name);
        }catch (Exception e){

            throw wrapException(e);
        }

        return c;

    }

    @Override
    public Long createSubCategory(SubCategoryDto dto) {
        if(dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly generated category", 400);
        }
        return null;
    }

    @Override
    public Long createSubSubCategory(SubSubCategoryDto dto) {
         return null;
    }

    @Override
    public List<CategoryDto> get() {
        return CategoryConverter.transform(categoryEJB.getAllCategories());
    }

    public CategoryDto getCategoryById(Long id) {
        return CategoryConverter.transform(categoryEJB.findCategoryById(id));
    }

    @Override
    public List<SubCategoryDto> getSubCategories() {
        return SubCategoryConverter.transform(subCategoryEJB.getAllSubCategories());
    }

    @Override
    public SubCategoryDto getSubCategoryById(Long subId) {
        return SubCategoryConverter.transform(subCategoryEJB.findSubCategoryById(subId));
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategories() {
        return SubSubCategoryConverter.transform(subSubCategoryEJB.getAllSubSubCategories());
    }

    @Override
    public SubSubCategoryDto getSubSubCategoryId(Long id) {
        return SubSubCategoryConverter.transform(subSubCategoryEJB.findSubSubCategoryById(id));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryEJB.deleteCategory(id);
    }

    @Override
    public void deleteSubCategory(Long id) {
        subCategoryEJB.deleteSubCategory(id);
    }

    @Override
    public void deleteSubSubCategory(Long id) {
        subSubCategoryEJB.deleteSubSubCategory(id);
    }

    private WebApplicationException wrapException(Exception e) throws WebApplicationException {

        /*
            Errors:
            4xx: the user has done something wrong, eg asking for something that does not exist (404)
            5xx: internal server error (eg, could be a bug in the code)
         */

        Throwable cause = Throwables.getRootCause(e);
        if (cause instanceof ConstraintViolationException) {
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
