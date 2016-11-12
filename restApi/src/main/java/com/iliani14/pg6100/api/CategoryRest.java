package com.iliani14.pg6100.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.iliani14.pg6100.dto.*;
import com.iliani14.pg6100.ejb.CategoryEJB;
import com.iliani14.pg6100.ejb.QuestionEJB;
import com.iliani14.pg6100.ejb.SubCategoryEJB;
import com.iliani14.pg6100.ejb.SubSubCategoryEJB;
import com.iliani14.pg6100.entity.SubSubCategory;
import io.swagger.annotations.ApiParam;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
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

    //CATEGORY

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
    public CategoryDto getById(Long id) {
        return CategoryConverter.transform(categoryEJB.findCategoryById(id));
    }

    @Override
    public List<CategoryDto> get() {return CategoryConverter.transform(categoryEJB.getAllCategories());}


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
    public void patchCategory(@ApiParam(ID_PARAM) Long id, @ApiParam("Modifying the category") String jsonPatch) {

        CategoryDto dto = CategoryConverter.transform(categoryEJB.findCategoryById(id));

        if( dto == null){
            throw new WebApplicationException("Cannot find category with id " + id, 404);
        }

        ObjectMapper jackson = new ObjectMapper();
        JsonNode jsonNode;
        try{
            jsonNode = jackson.readValue(jsonPatch,JsonNode.class);
        }catch (Exception e){
            throw new WebApplicationException("Invalid JSON data as input: " + e.getMessage(), 400);
        }
        if(jsonNode.has("id")) {
            throw new WebApplicationException(
                    "Cannot modify the category id from " + id + " to " + jsonNode.get("id"), 409);
        }

        String newName = dto.name;

        if(jsonNode.has("name")) {
            JsonNode nameNode = jsonNode.get("name");
            if (nameNode.isNull()) {
                newName = null;
            } else if (nameNode.isTextual()) {
                newName = nameNode.asText();
            } else {
                throw new WebApplicationException("Invalid JSON. Non-string title", 400);

            }
        }

        categoryEJB.updateCategory(id, newName);
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
    public void deleteCategory(Long id) {
        categoryEJB.deleteCategory(id);
    }


    //SUBCATEGORY

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
    public SubCategoryDto getSubCategoryById(Long id) {
        return SubCategoryConverter.transform(subCategoryEJB.findSubCategoryById(id));
    }

    @Override
    public List<SubCategoryDto> getAllSubCategories() { return SubCategoryConverter.transform(subCategoryEJB.getAllSubCategories());}


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
    public void patchSubCategory(@ApiParam(ID_PARAM) Long id, @ApiParam("Modifying the subcategory") String jsonPatch) {

        SubCategoryDto dto = SubCategoryConverter.transform(subCategoryEJB.findSubCategoryById(id));

        if( dto == null) {
            throw new WebApplicationException("Cannot find subcategory with id " + id, 404);
        }

        ObjectMapper jackson = new ObjectMapper();
        JsonNode jsonNode;

        try{
            jsonNode = jackson.readValue(jsonPatch, JsonNode.class);
        }catch (Exception e) {
            throw new WebApplicationException("Invalid JSON data as input: " + e.getMessage(), 400);
        }

        if (jsonNode.has("id")) {
            throw new WebApplicationException(
                    "Cannot modify the subcategory id from " + id + " to " + jsonNode.get("id"), 409);
        }


        if(jsonNode.has("categoryId")) {
            throw new WebApplicationException(
                    "Cannot modify the subcategory's id", 409);
        }

        String anotherName = dto.name;

        if (jsonNode.has("name")) {
            JsonNode nameNode = jsonNode.get("name");
            if (nameNode.isNull()) {
                anotherName= dto.name;
            } else
            if (nameNode.isTextual()) {
                anotherName = nameNode.asText();
            } else {
                throw new WebApplicationException("Invalid JSON. Non-string name", 400);
            }
        }

        subCategoryEJB.updateSubCategory(id, anotherName);
    }

    @Override
    public void deleteSubCategory(Long id) {
        subCategoryEJB.deleteSubCategory(id);
    }


    //SUBSUBCATEGORY

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
    public SubSubCategoryDto getSubSubCategoryById(Long id) {
        return SubSubCategoryConverter.transform(subSubCategoryEJB.findSubSubCategoryById(id));
    }


    @Override
    public List<SubSubCategoryDto> getAllSubSubCategories() { return SubSubCategoryConverter.transform(subSubCategoryEJB.getAllSubSubCategories());}


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
    public void patchSubSubCategory(@ApiParam(ID_PARAM) Long id, @ApiParam("Modifying the subsubcategory") String jsonPatch) {

        SubSubCategoryDto dto = SubSubCategoryConverter.transform(subSubCategoryEJB.findSubSubCategoryById(id));

        if(dto == null) {
            throw new WebApplicationException("Cannot find subsubcategory with id " + id, 404);
        }
        ObjectMapper jackson = new ObjectMapper();
        JsonNode jsonNode;

        try{
        jsonNode = jackson.readValue(jsonPatch, JsonNode.class);
        }catch (Exception e) {
            throw new WebApplicationException("Invalid JSON data as input: " + e.getMessage(), 400);
        }

        if(jsonNode.has("id")) {
            throw new WebApplicationException(
                    "Cannot modify the subsubcategory's id from " + id + " to " + jsonNode.get("id"), 409);
        }
        if(jsonNode.has("subcategoryId")) {
            throw new WebApplicationException(
                    "Cannot modify the id of subsubcategory", 400);
        }

        String someName = dto.name;

        if(jsonNode.has("name")) {
            JsonNode jn = jsonNode.get("name");
            if(jn.isNull()) {
                someName = dto.name;
            } else if(jn.isTextual()) {
                someName = jn.asText();
            } else {
                throw new WebApplicationException("Invalid JSON", 400);
            }
        }

        subSubCategoryEJB.updateSubSubCategory(id, someName);
    }

    @Override
    public void deleteSubSubCategory(Long id) {
        subSubCategoryEJB.deleteSubSubCategory(id);
    }


    //QUESTION

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
    public QuestionDto getQuestionById(Long id) {
        return QuestionConverter.transform(questionEJB.findQuestionById(id));
    }

    @Override
    public List<QuestionDto> getAllQuestions() { return QuestionConverter.transform(questionEJB.getAllQuestions());
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

    @Override
    public void patchQuestion(@ApiParam(ID_PARAM) Long id, @ApiParam("Modifying the question") String jsonPatch) {

    }

    @Override
    public void deleteQuestion(Long id) { questionEJB.deleteQuestion(id);

    }


    // METHODS RETRIEVING A LIST ...

    @Override
    public List<CategoryDto> getAllCategoriesWithAtLeastOneQuiz() {
        return CategoryConverter.transform(new ArrayList<>(categoryEJB.getAllCategoriesWithAtLeastOneQuiz()));
    }

    @Override
    public List<SubSubCategoryDto> getAllSubSubCategoriesWithAtLeastOneQuiz() {
        return SubSubCategoryConverter.transform(new ArrayList<SubSubCategory>(subSubCategoryEJB.getAllSubSubCategoriesWithAtLeastOneQuiz()));
    }

    @Override
    public List<SubCategoryDto> getAllSubCategoriesFromCategory(@ApiParam(ID_PARAM) Long id) {
        return SubCategoryConverter.transform(categoryEJB.getAllSubCategoriesForACategory(id));
    }

    @Override
    public List<SubCategoryDto> getAllSubCategoriesByParentId(@ApiParam(ID_PARAM) Long id) {
        return SubCategoryConverter.transform(categoryEJB.getAllSubCategoriesForACategory(id));
    }

    @Override
    public List<SubSubCategoryDto> getAllSubSubCategoriesFromSubCategory(@ApiParam(SUB_ID_PARAM) Long id) {
        return SubSubCategoryConverter.transform(subCategoryEJB.getAllSubSubCategoriesForSubCategory(id));
    }

    @Override
    public List<SubSubCategoryDto> getAllSubSubCategoriesFromParent(@ApiParam(SUB_ID_PARAM) Long id) {
        return SubSubCategoryConverter.transform(subCategoryEJB.getAllSubSubCategoriesForSubCategory(id));

    }

    @Override
    public List<QuestionDto> getAllQuestionsWithParent(@ApiParam(ID_PARAM) Long id) {
        return QuestionConverter.transform(categoryEJB.getAllQuizzesForCategory(id));
    }

    // DEPRECATED METHODS

    @Override
    public Response deprecatedGetById(@ApiParam(ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("category/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedGetSubCategoryById(@ApiParam(SUB_ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("category/subcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubCategoryById(@ApiParam(SUB_SUB_ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("category/subsubcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedGetQuestionById(@ApiParam(QUESTION_ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("category/questions/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedGetAllSubCategoriesFromCategory(@ApiParam(ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("category/" + id + "/subcategories").build())
                .build();
    }

    @Override
    public Response deprecatedGetAllSubCategoriesByParentId(@ApiParam(ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("category/" + id + "/subcategories").build())
                .build();
    }
    @Override
    public Response deprecatedGetAllSubSubCategoriesFromSubCategory(@ApiParam(SUB_ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("category//subcategories/" + id + "/subsubcategories").build())
                .build();}

    @Override
    public Response deprecatedGetAllSubSubCategoriesFromParent(@ApiParam(SUB_ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("category//subcategories/" + id + "/subsubcategories").build())
                .build();
    }

    private WebApplicationException wrapException(Exception e) throws WebApplicationException {
        Throwable cause = Throwables.getRootCause(e);
        if (cause instanceof ConstraintViolationException) {
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}