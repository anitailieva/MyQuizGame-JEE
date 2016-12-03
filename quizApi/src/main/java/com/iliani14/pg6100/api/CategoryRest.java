package com.iliani14.pg6100.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.iliani14.pg6100.dto.*;
import com.iliani14.pg6100.dto.collection.ListDto;
import com.iliani14.pg6100.dto.hal.HalLink;
import com.iliani14.pg6100.ejb.CategoryEJB;
import com.iliani14.pg6100.ejb.QuestionEJB;
import com.iliani14.pg6100.ejb.SubCategoryEJB;
import com.iliani14.pg6100.ejb.SubSubCategoryEJB;
import com.iliani14.pg6100.entity.Category;
import com.iliani14.pg6100.entity.Question;
import com.iliani14.pg6100.entity.SubCategory;
import com.iliani14.pg6100.entity.SubSubCategory;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Context
    UriInfo uriInfo;

    //CATEGORY

    @Override
    public Long createCategory(CategoryDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly generated category", 400);
        }
        Long id;
        try {
            id = categoryEJB.createCategory(dto.name);
        } catch (Exception e) {

            throw wrapException(e);
        }

        return id;

    }

    @Override
    public ListDto<CategoryDto> get(Integer offset, Integer limit, String withQuizzes, Boolean expand) {

        if(offset < 0){
            throw new WebApplicationException("Negative offset: " + offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: " + limit, 400);
        }

        if(expand == null) expand = false;

        int maxFromDb = 50;

        List<Category> categories;

        if(withQuizzes != null && (withQuizzes.isEmpty() || withQuizzes.equals("true"))) {
            categories = categoryEJB.getAllCategoriesWithAtLeastOneQuiz(maxFromDb);
        } else {
            categories = categoryEJB.getAllCategories(maxFromDb, expand);
        }

        if(offset != 0 && offset >=  categories.size()){
            throw new WebApplicationException("Offset "+ offset + " out of bound "+ categories.size(), 400);
        }

        ListDto<CategoryDto> dto = CategoryConverter.transform(
                categories, offset, limit, expand);

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/category")
                .queryParam("expand", expand)
                .queryParam("limit", limit);

        if(withQuizzes != null){
            builder = builder.queryParam("withQuizzes", withQuizzes);
        }

        dto._links.self = new HalLink(builder.clone()
                .queryParam("offset", offset)
                .build().toString()
        );

        if (!categories.isEmpty() && offset > 0) {
            dto._links.previous = new HalLink(builder.clone()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            );
        }
        if (offset + limit < categories.size()) {
            dto._links.next = new HalLink(builder.clone()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            );
        }

        return dto;

    }

    @Override
    public CategoryDto getById(@ApiParam(ID_PARAM) Long id,  @DefaultValue("false") Boolean expand) {
            if(expand == null) expand = false;
            return CategoryConverter.transform(categoryEJB.getCategory(id, expand));
    }


    @Override
    public void updateCategoryName(Long id, String name) {
        if (categoryEJB.findCategoryById(id) == null) {
            throw new WebApplicationException("Cannot find category with id: " + id, 404);
        }

        try {
            categoryEJB.updateCategory(id, name);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void patchCategory(@ApiParam(ID_PARAM) Long id, @ApiParam("Modifying the category") String jsonPatch) {

        CategoryDto dto = CategoryConverter.transform(categoryEJB.findCategoryById(id));

        if (dto == null) {
            throw new WebApplicationException("Cannot find category with id " + id, 404);
        }

        ObjectMapper jackson = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode.class);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid JSON data as input: " + e.getMessage(), 400);
        }
        if (jsonNode.has("id")) {
            throw new WebApplicationException(
                    "Cannot modify the category id from " + id + " to " + jsonNode.get("id"), 409);
        }

        String newName = dto.name;

        if (jsonNode.has("name")) {
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
        try {
            theId = Long.parseLong(dto.id);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid id: " + dto.id, 400);

        }

        if (id != theId) {
            // in this case, 409 (Conflict) sounds more appropriate than the generic 400
            throw new WebApplicationException("Now allowed to change the id of the resource", 409);
        }

        if (categoryEJB.findCategoryById(id) == null) {
            throw new WebApplicationException("Not allowed to create a category with PUT, and cannot find category with id: " + id, 404);
        }


        try {
            categoryEJB.updateCategory(id, dto.name);
        } catch (Exception e) {
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

        if (dto.categoryId == null) {
            throw new WebApplicationException("Cannot specify category id", 400);
        }
        Long id;
        try {
            id = subCategoryEJB.createSubCategory(Long.parseLong(dto.categoryId), dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }
        return id;
    }


    @Override
    public SubCategoryDto getSubCategoryById(Long id) {
        return SubCategoryConverter.transform(subCategoryEJB.findSubCategoryById(id));
    }

    @Override
    public ListDto<SubCategoryDto> getAllSubCategories(Integer offset, Integer limit) {

        if(offset < 0){
            throw new WebApplicationException("Negative offset: " + offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: " + limit, 400);
        }

        int maxFromDb = 50;

        List<SubCategory> list = subCategoryEJB.getSubCategoriesList(maxFromDb);

        if(offset != 0 && offset >= list.size()){
            throw new WebApplicationException("Offset "+ offset + " out of bound "+ list.size(), 400);
        }

        ListDto<SubCategoryDto> listDto = SubCategoryConverter.transform(list, offset, limit);
        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/category/subcategories")
                .queryParam("limit", limit);

        listDto._links.self = new HalLink(builder.clone()
            .queryParam("offset", offset)
            .build().toString());

        if(!list.isEmpty() && offset > 0){
            listDto._links.previous = new HalLink(builder.clone()
                .queryParam("offset", Math.max(offset + limit, 0))
                .build().toString());

        }

        return listDto;
    }


    @Override
    public void updateSubCategoryName(Long id, String name) {
        if (subCategoryEJB.findSubCategoryById(id) == null) {
            throw new WebApplicationException("Cannot find subcategory with id: " + id, 404);
        }

        try {
            subCategoryEJB.updateSubCategory(id, name);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void patchSubCategory(@ApiParam(ID_PARAM) Long id, @ApiParam("Modifying the subcategory") String jsonPatch) {

        SubCategoryDto dto = SubCategoryConverter.transform(subCategoryEJB.findSubCategoryById(id));

        if (dto == null) {
            throw new WebApplicationException("Cannot find subcategory with id " + id, 404);
        }

        ObjectMapper jackson = new ObjectMapper();
        JsonNode jsonNode;

        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode.class);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid JSON data as input: " + e.getMessage(), 400);
        }

        if (jsonNode.has("id")) {
            throw new WebApplicationException(
                    "Cannot modify the subcategory id from " + id + " to " + jsonNode.get("id"), 409);
        }


        if (jsonNode.has("categoryId")) {
            throw new WebApplicationException(
                    "Cannot modify the subcategory's id", 409);
        }

        String anotherName = dto.name;

        if (jsonNode.has("name")) {
            JsonNode nameNode = jsonNode.get("name");
            if (nameNode.isNull()) {
                anotherName = dto.name;
            } else if (nameNode.isTextual()) {
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
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly generated subsubcategory", 400);
        }

        if (dto.subcategoryId == null) {
            throw new WebApplicationException("Cannot specify subcategory id", 400);
        }

        Long id;
        Long parentId = Long.parseLong(dto.subcategoryId);
        try {
            id = subSubCategoryEJB.createSubSubCategory(parentId, dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return id;
    }

    @Override
    public ListDto<SubSubCategoryDto> getAllSubSubCategories(Integer offset, Integer limit,  String withQuizzes) {

        if(offset < 0){
            throw new WebApplicationException("Negative offset: " + offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: " + limit, 400);
        }

        int maxFromDb = 50;

        List<SubSubCategory> subSubCategories;

        if(withQuizzes != null && (withQuizzes.isEmpty() || withQuizzes.equals(true))) {
            subSubCategories = new ArrayList<>(subSubCategoryEJB.getAllSubSubCategoriesWithAtleastOneQuiz(maxFromDb));
        } else {
            subSubCategories = subSubCategoryEJB.getAllSubSubCategories();
        }

        if(offset != 0 && offset >= subSubCategories.size()){
            throw new WebApplicationException("Offset "+ offset + " out of bound "+ subSubCategories.size(), 400);
        }

        ListDto<SubSubCategoryDto> dto = SubSubCategoryConverter.transform(
                subSubCategories, offset, limit
        );

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/category/subsubcategories")
                .queryParam("limit", limit);

        if(withQuizzes != null){
            builder = builder.queryParam("withQuizzes", withQuizzes);
        }

        dto._links.self = new HalLink(builder.clone()
            .queryParam("offset", offset)
            .build().toString());

        if(!subSubCategories.isEmpty() && offset > 0) {
            dto._links.previous = new HalLink(builder.clone()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString());
        }


        if (offset + limit < subSubCategories.size()) {
            dto._links.next = new HalLink(builder.clone()
                 .queryParam("offset", offset + limit)
                 .build().toString()
                );
        }

        return dto;

    }

    @Override
    public SubSubCategoryDto getSubSubCategoryById(Long id) {
        return SubSubCategoryConverter.transform(subSubCategoryEJB.findSubSubCategoryById(id));
    }

    @Override
    public void updateSubSubCategoryName(Long id, String name) {
        if (subSubCategoryEJB.findSubSubCategoryById(id) == null) {
            throw new WebApplicationException("Cannot find subsubcategory with id: " + id, 404);
        }

        try {
            subSubCategoryEJB.updateSubSubCategory(id, name);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void patchSubSubCategory(@ApiParam(ID_PARAM) Long id, @ApiParam("Modifying the subsubcategory") String jsonPatch) {

        SubSubCategoryDto dto = SubSubCategoryConverter.transform(subSubCategoryEJB.findSubSubCategoryById(id));

        if (dto == null) {
            throw new WebApplicationException("Cannot find subsubcategory with id " + id, 404);
        }
        ObjectMapper jackson = new ObjectMapper();
        JsonNode jsonNode;

        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode.class);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid JSON data as input: " + e.getMessage(), 400);
        }

        if (jsonNode.has("id")) {
            throw new WebApplicationException(
                    "Cannot modify the subsubcategory's id from " + id + " to " + jsonNode.get("id"), 409);
        }
        if (jsonNode.has("subcategoryId")) {
            throw new WebApplicationException(
                    "Cannot modify the id of subsubcategory", 400);
        }

        String someName = dto.name;

        if (jsonNode.has("name")) {
            JsonNode jn = jsonNode.get("name");
            if (jn.isNull()) {
                someName = dto.name;
            } else if (jn.isTextual()) {
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

        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly generated question", 400);
        }


        Long parentId;
        try {
            parentId= Long.parseLong(dto.subSubCategoryId);
        } catch (NumberFormatException e) {
            throw new WebApplicationException("Id of the subsubcategory is not numeric", 400);
        }


        if (dto.subSubCategoryId == null) {
            throw new WebApplicationException("Cannot specify subsubcategory id", 400);
        }


        Long id;

        try {
            id = questionEJB.createQuestion(parentId, dto.question, dto.answers, dto.theCorrectAnswer);
        } catch (Exception e) {

            throw wrapException(e);
        }

        return id;

    }

    @Override
    public ListDto<QuestionDto> getAllQuestions( Integer offset, @DefaultValue("10") Integer limit) {

        if(offset < 0){
            throw new WebApplicationException("Negative offset: "+offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: "+limit, 400);
        }

        int maxFromDb = 50;

        List<Question> list = questionEJB.getListOfQuestions(maxFromDb);

        if(offset != 0 && offset >=  list.size()){
            throw new WebApplicationException("Offset "+ offset + " out of bound "+ list.size(), 400);
        }

        ListDto<QuestionDto> listDto = QuestionConverter.transform(list, offset, limit);

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/category/questions")
                .queryParam("limit", limit);

        listDto._links.self = new HalLink(builder.clone()
                .queryParam("offset", offset)
                .build().toString()
        );

        if (!list.isEmpty() && offset > 0) {
            listDto._links.previous = new HalLink(builder.clone()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            );
        }
        if (offset + limit < list.size()) {
            listDto._links.next = new HalLink(builder.clone()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            );
        }

        return listDto;
    }

    @Override
    public QuestionDto getQuestionById(Long id) {
        return QuestionConverter.transform(questionEJB.findQuestionById(id));
    }

    @Override
    public void updateQuestion(Long id, String question) {
        if (questionEJB.findQuestionById(id) == null) {
            throw new WebApplicationException("Cannot find question with id: " + id, 404);
        }

        try {
            questionEJB.updateQuestion(id, question);
        } catch (Exception e) {
            throw wrapException(e);
        }

    }

    @Override
    public void patchQuestion(@ApiParam(ID_PARAM) Long id, @ApiParam("Modifying the question") String jsonPatch) {
        QuestionDto dto = QuestionConverter.transform(questionEJB.findQuestionById(id));

        if (dto == null) {
            throw new WebApplicationException("Cannot find quiz with id " + id, 404);
        }

        ObjectMapper jackson = new ObjectMapper();

        JsonNode jsonNode;
        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode.class);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid JSON data as input: " + e.getMessage(), 400);
        }

        if (jsonNode.has("id")) {
            throw new WebApplicationException(
                    "Cannot modify the question id from " + id + " to " + jsonNode.get("id"), 409);
        }

        if (jsonNode.has("subSubCategoryId")) {
            throw new WebApplicationException(
                    "Cannot modify the id of questions's parent category", 400);
        }

        String updatedQuestion = "Updated";

        if (jsonNode.has("question")) {
            JsonNode node = jsonNode.get("question");
            if (node.isNull()) {
                updatedQuestion = dto.question;
            } else if (node.isTextual()) {
                updatedQuestion = node.asText();
            } else {
                throw new WebApplicationException("Invalid JSON. Non-string title", 400);

            }

        }


        questionEJB.updateQuestion(id, updatedQuestion);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionEJB.deleteQuestion(id);

    }

    // METHODS RETRIEVING A LIST ...


    @Override
    public ListDto<SubCategoryDto> getAllSubCategoriesFromCategory(@ApiParam(ID_PARAM) Long id, @ApiParam("Offset in the list of categories") @DefaultValue("0") Integer offset, @ApiParam("Limit of subcategories in a single retrieved page") @DefaultValue("10") Integer limit) {

        if(offset < 0){
            throw new WebApplicationException("Negative offset: "+offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: "+limit, 400);
        }

        int maxFromDb = 50;

        List<SubCategory> list = categoryEJB.getAllSubCategoriesForACategory(id, maxFromDb);

        if(offset != 0 && offset >= list.size()) {
            throw new WebApplicationException("Offset "+ offset + " out of bound "+ list.size(), 400);
        }
        ListDto<SubCategoryDto> listDto = SubCategoryConverter.transform(list, offset, limit);

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/category/" + id + "/subcategories")
                .queryParam("limit", limit);

        listDto._links.self = new HalLink(builder.clone()
            .queryParam("offset", offset)
            .build().toString());

        if(!list.isEmpty() && offset > 0){
            listDto._links.previous = new HalLink(builder.clone()
            .queryParam("offset", Math.max(offset - limit, 0))
            .build().toString());
        }

        if(offset + limit < list.size()) {
            listDto._links.next = new HalLink(builder.clone()
            .build().toString());
        }

        return listDto;
    }

    @Override
    public ListDto<SubSubCategoryDto> getAllSubSubCategoriesFromSubCategory(@ApiParam(SUB_ID_PARAM) Long id, @ApiParam("Offset in the list of categories") @DefaultValue("0") Integer offset, @ApiParam("Limit of subsubcategories in a single retrieved page") @DefaultValue("10") Integer limit) {
        if(offset < 0){
            throw new WebApplicationException("Negative offset: "+offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: "+limit, 400);
        }

        int maxFromDb = 50;

        List<SubSubCategory> list = subCategoryEJB.getAllSubSubCategoriesForSubCategory(id, maxFromDb);

        if(offset != 0 && offset >= list.size()) {
            throw new WebApplicationException("Offset "+ offset + " out of bound "+ list.size(), 400);
        }
        ListDto<SubSubCategoryDto> listDto = SubSubCategoryConverter.transform(list, offset, limit);

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/category/subcategories/" + id + "/subsubcategories")
                .queryParam("limit", limit);

        listDto._links.self = new HalLink(builder.clone()
                .queryParam("offset", offset)
                .build().toString());

        if(!list.isEmpty() && offset > 0){
            listDto._links.previous = new HalLink(builder.clone()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString());
        }

        if(offset + limit < list.size()) {
            listDto._links.next = new HalLink(builder.clone()
                    .build().toString());
        }

        return listDto;
    }

    @Override
    public ListDto<QuestionDto> getAllQuestionsWithParent(@ApiParam("Offset in the list of categories") @DefaultValue("0") Integer offset, @ApiParam("Limit of questions in a single retrieved page") @DefaultValue("10") Integer limit, @ApiParam(ID_PARAM) Long id) {
        if(offset < 0){
            throw new WebApplicationException("Negative offset: "+offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: "+limit, 400);
        }

        int maxFromDb = 50;

        List<Question> list = categoryEJB.getAllQuestionsForCategory(id, maxFromDb);

        if( offset != 0 && offset >= list.size()) {
            throw new WebApplicationException("Offset "+ offset + " out of bound "+ list.size(), 400);
        }
        ListDto<QuestionDto> listDto = QuestionConverter.transform(list, offset, limit);

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/category/quizzes/parent/" + id)
                .queryParam("limit", limit);

        listDto._links.self = new HalLink(builder.clone()
            .queryParam("offset", offset)
            .build().toString());

        if (!list.isEmpty() && offset > 0) {
            listDto._links.previous = new HalLink(builder.clone()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            );
        }
        if (offset + limit < list.size()) {
            listDto._links.next = new HalLink(builder.clone()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            );
        }

        return listDto;
    }


    @Override
    public Response getRandomQuiz(@ApiParam("ID of category/subcategory/subsubcategory to get a quiz from") Long id) {
        Long quizId;
        Random r = new Random();

        if (questionEJB.getAllQuestions().isEmpty()) {
            return Response.status(404).build();
        }


       if (categoryEJB.findCategoryById(id) != null) {

            quizId = categoryEJB.getRandomQuizzesForCategory(id, 1).get(0);

            if (quizId == null) {
                return Response.status(404).build();
            }

            return Response.status(307)
                    .location(URI.create("category/questions/" + quizId))
                    .build();


        } else if (subCategoryEJB.findSubCategoryById(id) != null) {

            quizId = subCategoryEJB.getRandomQuizzesForSubCategory(id, 1).get(0);

            if (quizId == null) {
                return Response.status(404).build();
            }

            return Response.status(307)
                    .location(URI.create("category/questions/" + quizId))
                    .build();

            } else if (subSubCategoryEJB.findSubSubCategoryById(id) != null) {

            quizId = subSubCategoryEJB.getRandomQuizzesForSubSubCategory(id, 1).get(0);

            if (quizId == null) {
                return Response.status(404).build();
            }

            return Response.status(307)
                    .location(URI.create("category/questions/" + quizId))
                    .build();

        }

        return Response.status(307)
                .location(URI.create("category/questions/" + questionEJB.getRandomQuizzes(1).get(0)))
                .build();
    }

    @Override
    public IdDto getRandomQuizzes(String limit,  String filter) {
        if(questionEJB.getAllQuestions().isEmpty()) {
            throw new WebApplicationException("There are no quizzes yet.", 404);
        }

        int numberOfQuestions;
        if(!Strings.isNullOrEmpty(limit)) {
            try {
                numberOfQuestions = Integer.parseInt(limit);
            } catch (NumberFormatException e) {
                throw new WebApplicationException("Id of the root category is not numeric", 400);
            }
        } else {
            numberOfQuestions = 5;
        }

        String[] parts;

        IdDto dtos = new IdDto();
        dtos.ids = new ArrayList<>();
        ArrayList<Long> quizIds;

        if (!Strings.isNullOrEmpty(filter)) {
            parts = filter.split("_");
            if(parts.length != 2)
                throw new WebApplicationException("Filter value has incorrect format", 404);

            if(!StringUtils.isNumeric(parts[1])){
                throw new WebApplicationException("Filter value has incorrect format", 404);
            }

            switch (parts[0]){
                case "c":
                    Long catId = Long.parseLong(parts[1]);

                    quizIds = new ArrayList<>(categoryEJB.getRandomQuizzesForCategory(catId, numberOfQuestions));

                    if(quizIds.size() < numberOfQuestions) {
                        throw new WebApplicationException("There are not enough quizzes.", 404);
                    }

                    dtos.ids.addAll(quizIds);
                    return dtos;
                case "s":
                    Long subcategoryId = Long.parseLong(parts[1]);

                    quizIds = new ArrayList<>(subCategoryEJB.getRandomQuizzesForSubCategory(subcategoryId, numberOfQuestions));

                    if(quizIds.size() < numberOfQuestions) {
                        throw new WebApplicationException("There are not enough quizzes.", 404);
                    }

                    dtos.ids.addAll(quizIds);
                    return dtos;
                case "ss":
                    Long specifyingCategoryId = Long.parseLong(parts[1]);

                    quizIds = new ArrayList<>(subSubCategoryEJB.getRandomQuizzesForSubSubCategory(specifyingCategoryId, numberOfQuestions));

                    if(quizIds.size() < numberOfQuestions) {
                        throw new WebApplicationException("There are not enough quizzes.", 404);
                    }

                    dtos.ids.addAll(quizIds);
                    return dtos;
                default:
                    throw new WebApplicationException("Filter value has incorrect format", 404);
            }
        }

        quizIds = new ArrayList<>(questionEJB.getRandomQuizzes(numberOfQuestions));

        if(quizIds.size() < numberOfQuestions) {
            throw new WebApplicationException("There are not enough quizzes.", 404);
        }

        dtos.ids.addAll(quizIds);
        return dtos;
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
                .location(UriBuilder.fromUri("category/subcategories/" + id + "/subsubcategories").build())
                .build();}

    @Override
    public Response deprecatedGetAllSubSubCategoriesFromParent(@ApiParam(SUB_ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("category/subcategories/" + id + "/subsubcategories").build())
                .build();
    }

    @Override
    public Response deprecatedGetAllCategoriesWithAtLeastOneQuiz() {
        return Response.status(301)
                .location(UriBuilder.fromUri("category").queryParam("withQuizzes", "").build())
                .build();
    }

    @Override
    public Response deprecatedGetAllSubSubCategoriesWithAtLeastOneQuiz() {
        return Response.status(301)
                .location(UriBuilder.fromUri("category/subsubcategories").queryParam("withQuizzes", "").build())
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