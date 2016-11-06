package com.iliani14.pg6100.api;

import com.iliani14.pg6100.dto.CategoryDto;
import com.iliani14.pg6100.dto.QuestionDto;
import com.iliani14.pg6100.dto.SubCategoryDto;
import com.iliani14.pg6100.dto.SubSubCategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by anitailieva on 27/10/2016.
 */
@Api(value = "/category", description = "Handling and retrieving categories" )
@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
public interface CategoryRestApi {

    @ApiOperation("Create a category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created category")
    Long createCategory(
            @ApiParam("Name and id of the category")
                    CategoryDto dto);


    @ApiOperation("Create a subcategory")
    @POST
    @Path("/subcategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created subcategory")
    Long createSubCategory(
            @ApiParam("Id of subcategory, id of category and subcategory name")
                    SubCategoryDto dto);

    @ApiOperation("Create a subsubcategory")
    @POST
    @Path("/subsubcategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created subsubcategory")
    Long createSubSubCategory(
            @ApiParam("Id of the subsubcategory, Id of the subcategory and name of subsubcategory ")
                    SubSubCategoryDto dto);


    @ApiOperation("Create a question")
    @POST
    @Path("/questions")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created question")
    Long createQuestion(
            @ApiParam("Id of the question, id of subsubcategory,  the question, list of answers and the correct answer")
                        QuestionDto dto);

    @ApiOperation("Get all the categories")
    @GET
    List<CategoryDto> get();


    @ApiOperation("Get a category by id")
    @GET
    @Path("/id/{id}")
    CategoryDto getCategoryById(
            @ApiParam("The id of the category")
            @PathParam("id")
                    Long id);


    @ApiOperation("Get all the subcategories")
    @GET
    @Path("/subcategories")
    List<SubCategoryDto> getAllSubCategories();


    @ApiOperation("Get a subcategory by id")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/subcategories/id/{id}")
    SubCategoryDto getSubCategoryById(
            @ApiParam("id")
            @PathParam("id")
                    Long id);



    @ApiOperation("Get all the subsubcategories")
    @GET
    @Path("/subsubcategories")
    List<SubSubCategoryDto> getAllSubSubCategories();

    @ApiOperation("Get a subsubcategory by id")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/subsubcategories/id/{id}")
    SubSubCategoryDto getSubSubCategoryById(
            @ApiParam("id")
            @PathParam("id")
                    Long id
    );

    @ApiOperation("Get all questions")
    @GET
    @Path("/questions")
    List<QuestionDto> getAllQuestions();


    @ApiOperation("Get a question by id")
    @GET
    @Path("/questions/id/{id}")
    QuestionDto getQuestionById(
            @ApiParam("id")
            @PathParam("id")
                    Long id);


    @ApiOperation("Delete a category")
    @DELETE
    @Path("/id/{id}")
    void deleteCategory(
            @ApiParam("Category Id")
            @PathParam("id")
                    Long id);


    @ApiOperation("Delete a subcategory")
    @DELETE
    @Path("/subcategories/id/{id}")
    void deleteSubCategory(
            @ApiParam("SubCategory Id")
            @PathParam("id")
                    Long id
    );

    @ApiOperation("Delete a subsubcategory")
    @DELETE
    @Path("/subsubcategories/id/{id}")
    void deleteSubSubCategory(
            @ApiParam("SubSubCategory id")
            @PathParam("id")
                    Long id);



    @ApiOperation("Delete a question")
    @DELETE
    @Path("/questions/id/{id}")
    void deleteQuestion(
            @ApiParam("Question Id")
            @PathParam("id")
                    Long id
    );

    @ApiOperation("Update an existing category")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void update(
            @ApiParam("id")
            @PathParam("id")
                    Long id,
            //
            @ApiParam("The category that will replace the old one. But cannot change its id.")
                    CategoryDto dto);

    @ApiOperation("Update the name of a category")
    @PUT
    @Path("/id/{id}/name")
    @Consumes(MediaType.TEXT_PLAIN)
    void updateCategoryName(
            @ApiParam("id")
            @PathParam("id")
                    Long id,

            @ApiParam("The category name which will replace the old one")
                    String name
    );

    @ApiOperation("Update the name of a subcategory")
    @PUT
    @Path("/subcategories/id/{id}/name")
    @Consumes(MediaType.TEXT_PLAIN)
    void updateSubCategoryName(
            @ApiParam("id")
            @PathParam("id")
                    Long id,

            @ApiParam("The subcategory name which will replace the old one")
                    String name
    );

    @ApiOperation("Update the name of a subsubcategory")
    @PUT
    @Path("/subsubcategories/id/{id}/name")
    @Consumes(MediaType.TEXT_PLAIN)
    void updateSubSubCategoryName(
            @ApiParam("id")
            @PathParam("id")
                    Long id,

            @ApiParam("The subsubcategory name which will replace the old one")
                    String name
    );

    @ApiOperation("Update the text of a question")
    @PUT
    @Path("/questions/id/{id}/question")
    @Consumes(MediaType.TEXT_PLAIN)
    void updateQuestion(
            @ApiParam("id")
            @PathParam("id")
                    Long id,

            @ApiParam("The subsubcategory name which will replace the old one")
                    String question
    );

}