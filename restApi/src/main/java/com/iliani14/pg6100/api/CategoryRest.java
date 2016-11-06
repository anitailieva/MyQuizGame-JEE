package com.iliani14.pg6100.api;

import com.google.common.base.Throwables;
import com.iliani14.pg6100.dto.*;
import com.iliani14.pg6100.ejb.CategoryEJB;
import com.iliani14.pg6100.ejb.QuestionEJB;
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

    @EJB
    private QuestionEJB questionEJB;

    @Override
    public Long createCategory(CategoryDto dto) {
        if(dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly generated category", 400);
        }
        Long id;
        try{
            id = categoryEJB.createCategory(dto.name);
        }catch (Exception e){

            throw wrapException(e);
        }

        return id;

    }

    @Override
    public Long createSubCategory(SubCategoryDto dto) {

        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly generated subcategory", 400);
        }

        if (dto.categoryId == null){
            throw new WebApplicationException("Cannot specify category id", 400);
        }
        Long id;
        try{
            id = subCategoryEJB.createSubCategory(Long.parseLong(dto.categoryId), dto.name);
        }catch (Exception e){
            throw  wrapException(e);
        }
        return id;
    }

    @Override
    public Long createSubSubCategory(SubSubCategoryDto dto) {
        if (dto.id != null){
            throw new WebApplicationException("Cannot specify id for a newly generated subsubcategory", 400);
        }

        if (dto.subcategoryId == null){
            throw new WebApplicationException("Cannot specify subcategory id", 400);
        }

        Long id;
        Long parentId = Long.parseLong(dto.subcategoryId);
        try{
            id = subSubCategoryEJB.createSubSubCategory(parentId, dto.name);
        }catch (Exception e){
            throw wrapException(e);
        }

        return id;
    }

    @Override
    public Long createQuestion(QuestionDto dto) {

        if(dto.id != null){
            throw new WebApplicationException("Cannot specify id for a newly generated question", 400);
        }

        if (dto.subSubCategoryId == null){
            throw new WebApplicationException("Cannot specify subsubcategory id", 400);
        }


        Long id;
        Long parentId = Long.parseLong(dto.subSubCategoryId);
        try{
            id = questionEJB.createQuestion(parentId, dto.question, dto.answers, dto.theCorrectAnswer);
        }catch (Exception e){

            throw wrapException(e);
        }

        return id;

    }

    @Override
    public List<CategoryDto> get() {return CategoryConverter.transform(categoryEJB.getAllCategories());}

    @Override
    public List<SubCategoryDto> getAllSubCategories() { return SubCategoryConverter.transform(subCategoryEJB.getAllSubCategories());}

    @Override
    public List<SubSubCategoryDto> getAllSubSubCategories() { return SubSubCategoryConverter.transform(subSubCategoryEJB.getAllSubSubCategories());}

    @Override
    public List<QuestionDto> getAllQuestions() { return QuestionConverter.transform(questionEJB.getAllQuestions());
    }


    @Override
    public CategoryDto getCategoryById(Long id) {
        return CategoryConverter.transform(categoryEJB.findCategoryById(id));
    }

    @Override
    public SubCategoryDto getSubCategoryById(Long id) {
        return SubCategoryConverter.transform(subCategoryEJB.findSubCategoryById(id));
    }

    @Override
    public SubSubCategoryDto getSubSubCategoryById(Long id) {
        return SubSubCategoryConverter.transform(subSubCategoryEJB.findSubSubCategoryById(id));
    }

    @Override
    public QuestionDto getQuestionById(Long id) {
        return QuestionConverter.transform(questionEJB.findQuestionById(id));
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

    @Override
    public void deleteQuestion(Long id) { questionEJB.deleteQuestion(id);

    }

    @Override
    public void update(Long id, CategoryDto dto) {
        long theId;
        try{
            theId = Long.parseLong(dto.id);
        }catch (Exception e){
            throw new WebApplicationException("Invalid id: "+dto.id, 400);

        }

        if(id != theId){
            // in this case, 409 (Conflict) sounds more appropriate than the generic 400
            throw new WebApplicationException("Now allowed to change the id of the resource", 409);
        }

        if(categoryEJB.findCategoryById(id) == null){
            throw new WebApplicationException("Not allowed to create a category with PUT, and cannot find category with id: "+id, 404);
        }


        try{
            categoryEJB.updateCategory(id, dto.name);
        }catch (Exception e){
            throw wrapException(e);
        }
    }

    @Override
    public void updateCategoryName(Long id, String name) {
        if(categoryEJB.findCategoryById(id) == null){
            throw new WebApplicationException("Cannot find category with id: "+id, 404);
        }

        try {
            categoryEJB.updateCategory(id, name );
        } catch (Exception e){
            throw wrapException(e);
        }
    }

    @Override
    public void updateSubCategoryName(Long id, String name) {
        if(subCategoryEJB.findSubCategoryById(id) == null){
            throw new WebApplicationException("Cannot find subcategory with id: "+id, 404);
        }

        try {
            subCategoryEJB.updateSubCategory(id, name);
        } catch (Exception e){
            throw wrapException(e);
        }
    }

    @Override
    public void updateSubSubCategoryName(Long id, String name) {
        if(subSubCategoryEJB.findSubSubCategoryById(id) == null) {
            throw new WebApplicationException("Cannot find subsubcategory with id: "+id, 404);
        }

        try{
            subSubCategoryEJB.updateSubSubCategory(id, name);
        }catch (Exception e){
            throw  wrapException(e);
        }
    }

    @Override
    public void updateQuestion(Long id, String question) {
        if(questionEJB.findQuestionById(id) == null) {
            throw new WebApplicationException("Cannot find question with id: "+id, 404);
        }

        try{
            questionEJB.updateQuestion(id, question);
        }catch (Exception e) {
            throw wrapException(e);
        }

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