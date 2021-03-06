package com.iliani14.pg6100.api;

import com.iliani14.pg6100.dto.*;
import com.iliani14.pg6100.dto.collection.ListDto;
import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by anitailieva on 27/10/2016.
 */
@Api(value = "/category", description = "Handling and retrieving categories" )
@Path("/category")
@Produces(Formats.V1_JSON)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public interface CategoryRestApi {

    String ID_PARAM = "The numeric id of the category";
    String SUB_ID_PARAM = "The numeric id of the subcategory";
    String SUB_SUB_ID_PARAM = "The numeric id of the subsubcategory";
    String QUESTION_ID_PARAM = "The numeric id of the question";


    // CATEGORY

    @ApiOperation("Create a category")
    @POST
    @Consumes(Formats.V1_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created category")
    Long createCategory(
            @ApiParam("Name and id of the category")
                    CategoryDto dto);


    @ApiOperation("Get all the categories with a quiz")
    @GET
    ListDto<CategoryDto> get(
            @ApiParam("Offset in the list of categories")
            @QueryParam("offset")
            @DefaultValue("0")
                Integer offset,
            @ApiParam("Limit of categories in a single retrieved page")
            @QueryParam("limit")
            @DefaultValue("10")
                Integer limit,
            @ApiParam("Retrieving categories with quizzes")
            @QueryParam("withQuizzes")
                String withQuizzes,
            @ApiParam("Whether to retrieve or not categories.")
            @QueryParam("expand")
            @DefaultValue("false")
                Boolean expand
    );


    @ApiOperation("Get a category specified by id")
    @GET
    @Path("/{id}")
    CategoryDto getById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,
             @ApiParam("Whether to retrieve or not categories by given id.")
             @QueryParam("expand")
             @DefaultValue("false")
                     Boolean expand);


    @ApiOperation("Update an existing category")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void update(
            @ApiParam(ID_PARAM)
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
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The category name which will replace the old one")
                    String name
    );

    @ApiOperation("Modify the category")
    @Path("/id/{id}")
    @PATCH
    @Consumes(Formats.V1_JSON_MERGE)
    void patchCategory(@ApiParam(ID_PARAM)
                       @PathParam("id")
                        Long id,

                       @ApiParam("Modifying the category")
                               String jsonPatch);

    @ApiOperation("Delete a category")
    @DELETE
    @Path("/id/{id}")
    void deleteCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id);


    // SUBCATEGORY

    @ApiOperation("Create a subcategory")
    @POST
    @Path("/subcategories")
    @Consumes(Formats.V1_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created subcategory")
    Long createSubCategory(
            @ApiParam("Id of subcategory, id of category and subcategory name")
                    SubCategoryDto dto);

    @ApiOperation("Get all the subcategories")
    @GET
    @Path("/subcategories")
    ListDto<SubCategoryDto> getAllSubCategories(
            @ApiParam("Offset in the list of categories")
            @QueryParam("offset")
            @DefaultValue("0")
                    Integer offset,
            @ApiParam("Limit of subcategories in a single retrieved page")
            @QueryParam("limit")
            @DefaultValue("10")
                    Integer limit
    );


    @ApiOperation("Get a subcategory by id")
    @GET
    @Path("/subcategories/{id}")
    SubCategoryDto getSubCategoryById(
            @ApiParam(SUB_ID_PARAM)
            @PathParam("id")
                    Long id);


    @ApiOperation("Update the name of a subcategory")
    @PUT
    @Path("/subcategories/id/{id}/name")
    @Consumes(MediaType.TEXT_PLAIN)
    void updateSubCategoryName(
            @ApiParam(SUB_ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The subcategory name which will replace the old one")
                    String name
    );

    @ApiOperation("Modify the subcategory")
    @Path("/subcategories/id/{id}")
    @PATCH
    @Consumes(Formats.V1_JSON_MERGE)
    void patchSubCategory(@ApiParam(ID_PARAM)
                       @PathParam("id")
                               Long id,

                       @ApiParam("Modifying the subcategory")
                               String jsonPatch);


    @ApiOperation("Delete a subcategory")
    @DELETE
    @Path("/subcategories/id/{id}")
    void deleteSubCategory(
            @ApiParam(SUB_ID_PARAM)
            @PathParam("id")
                    Long id
    );


    @ApiOperation("Create a subsubcategory")
    @POST
    @Path("/subsubcategories")
    @Consumes(Formats.V1_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created subsubcategory")
    Long createSubSubCategory(
            @ApiParam("Id of the subsubcategory, Id of the subcategory and name of subsubcategory ")
                    SubSubCategoryDto dto);


    @ApiOperation("Get all the subsubcategories")
    @GET
    @Path("/subsubcategories")
    ListDto<SubSubCategoryDto> getAllSubSubCategories(
            @ApiParam("Offset in the list of categories")
            @QueryParam("offset")
            @DefaultValue("0")
                    Integer offset,
            @ApiParam("Limit of subsubcategories in a single retrieved page")
            @QueryParam("limit")
            @DefaultValue("10")
                    Integer limit,
            @ApiParam("Retrieving subsubcategories with quizzes")
            @QueryParam("withQuizzes")
                    String withQuizzes
    );


    @ApiOperation("Get a subsubcategory by id")
    @GET
    @Path("/subsubcategories/{id}")
    SubSubCategoryDto getSubSubCategoryById(
            @ApiParam(SUB_SUB_ID_PARAM)
            @PathParam("id")
                    Long id
    );

    @ApiOperation("Update the name of a subsubcategory")
    @PUT
    @Path("/subsubcategories/id/{id}/name")
    @Consumes(MediaType.TEXT_PLAIN)
    void updateSubSubCategoryName(
            @ApiParam(SUB_SUB_ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The subsubcategory name which will replace the old one")
                    String name
    );

    @ApiOperation("Modify the subsubcategory")
    @Path("/subsubcategories/id/{id}")
    @PATCH
    @Consumes(Formats.V1_JSON_MERGE)
    void patchSubSubCategory(@ApiParam(ID_PARAM)
                          @PathParam("id")
                                  Long id,

                          @ApiParam("Modifying the subsubcategory")
                                  String jsonPatch);



    @ApiOperation("Delete a subsubcategory")
    @DELETE
    @Path("/subsubcategories/id/{id}")
    void deleteSubSubCategory(
            @ApiParam(SUB_SUB_ID_PARAM)
            @PathParam("id")
                    Long id);



    @ApiOperation("Create a question")
    @POST
    @Path("/questions")
    @Consumes(Formats.V1_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created question")
    Long createQuestion(
            @ApiParam("Id of the question, id of subsubcategory,  the question, list of answers and the correct answer")
                    QuestionDto dto);


    @ApiOperation("Get all questions")
    @GET
    @Path("/questions")
    @Produces(Formats.HAL_V1)
    ListDto<QuestionDto> getAllQuestions(
            @ApiParam("Offset in the list of categories")
            @QueryParam("offset")
            @DefaultValue("0")
                    Integer offset,
            @ApiParam("Limit of questions in a single retrieved page")
            @QueryParam("limit")
            @DefaultValue("10")
                    Integer limit
    );



    @ApiOperation("Get a question by id")
    @GET
    @Path("/questions/{id}")
    QuestionDto getQuestionById(
            @ApiParam(QUESTION_ID_PARAM)
            @PathParam("id")
                    Long id);

    @ApiOperation("Update the text of a question")
    @PUT
    @Path("/questions/id/{id}/question")
    @Consumes(MediaType.TEXT_PLAIN)
    void updateQuestion(
            @ApiParam(QUESTION_ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The subsubcategory name which will replace the old one")
                    String question
    );

    @ApiOperation("Modify the question")
    @Path("/questions/id/{id}")
    @PATCH
    @Consumes(Formats.V1_JSON_MERGE)
    void patchQuestion(@ApiParam(ID_PARAM)
                          @PathParam("id")
                                  Long id,

                          @ApiParam("Modifying the question")
                                  String jsonPatch);


    @ApiOperation("Delete a question")
    @DELETE
    @Path("/questions/id/{id}")
    void deleteQuestion(
            @ApiParam(QUESTION_ID_PARAM)
            @PathParam("id")
                    Long id
    );

    @ApiOperation("Get all subcategories with a specified category id")
    @GET
    @Path("/{id}/subcategories")
    ListDto<SubCategoryDto> getAllSubCategoriesFromCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id,
            @ApiParam("Offset in the list of categories")
            @QueryParam("offset")
            @DefaultValue("0")
                    Integer offset,
            @ApiParam("Limit of subcategories in a single retrieved page")
            @QueryParam("limit")
            @DefaultValue("10")
                    Integer limit
    );


    @ApiOperation("Get all subsubcategories with a specified subcategory id")
    @GET
    @Path("/subcategories/{id}/subsubcategories")
    ListDto<SubSubCategoryDto> getAllSubSubCategoriesFromSubCategory(
            @ApiParam(SUB_ID_PARAM)
            @PathParam("id")
                    Long id,
            @ApiParam("Offset in the list of categories")
            @QueryParam("offset")
            @DefaultValue("0")
                    Integer offset,
            @ApiParam("Limit of subsubcategories in a single retrieved page")
            @QueryParam("limit")
            @DefaultValue("10")
                    Integer limit
    );

    @ApiOperation("Get all questions with parent specified by id")
    @GET
    @Path("/quizzes/parent/{id}")
    @Produces(Formats.HAL_V1)
    ListDto<QuestionDto> getAllQuestionsWithParent(
            @ApiParam("Offset in the list of categories")
            @QueryParam("offset")
            @DefaultValue("0")
                    Integer offset,
            @ApiParam("Limit of questions in a single retrieved page")
            @QueryParam("limit")
            @DefaultValue("10")
                    Integer limit,
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id
    );

    @ApiOperation("Retrieve a category(subcategory/subsubcategory with a random quiz")
    @ApiResponses({
            @ApiResponse(code = 307, message = "Temporary redirect to your quiz!"),
            @ApiResponse(code = 404, message = "No categories can be found with that id")
    })
    @GET
    @Path(("/randomQuiz"))
    Response getRandomQuiz(
            @ApiParam("ID of category/subcategory/subsubcategory to get a quiz from")
            @QueryParam("filter")
                   Long id);




    @ApiOperation("Get a random quiz. Return a random quiz for category, sub- or subsubcategory" +
            "depending on filter")
    @POST
    @Path(("/randomQuizzes"))
    IdDto getRandomQuizzes(
            @ApiParam("Number of quizzes")
            @QueryParam("limit")
                    String limit,
            @ApiParam("Filter randomquizzes by category id")
            @QueryParam("filter")
                    String filter
    );
    //endregion

    // DEPRECATED

    @ApiOperation("Get a category by id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id);



   @ApiOperation("Get a subcategory by id")
   @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/subcategories/id/{id}")
    @Deprecated
    Response deprecatedGetSubCategoryById(
            @ApiParam(SUB_ID_PARAM)
            @PathParam("id")
            Long id);


    @ApiOperation("Get a subsubcategory by id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/subsubcategories/id/{id}")
    @Deprecated
    Response deprecatedGetSubSubCategoryById(
            @ApiParam(SUB_SUB_ID_PARAM)
            @PathParam("id")
            Long id);

    @ApiOperation("Get a question by id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/questions/id/{id}")
    @Deprecated
    Response deprecatedGetQuestionById(
            @ApiParam(QUESTION_ID_PARAM)
            @PathParam("id")
                    Long id);



    @ApiOperation("Get all subcategories with a specified category id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/id/{id}/subcategories")
    @Deprecated
    Response deprecatedGetAllSubCategoriesFromCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id
    );

    @ApiOperation("Get all subcategories with a specified parent id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/subcategories/parent/{id}")
    @Deprecated
    Response deprecatedGetAllSubCategoriesByParentId(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id
    );


    @ApiOperation("Get all subsubcategories with a specified subcategory id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/subcategories/id/{id}/subsubcategories")
    @Deprecated
    Response deprecatedGetAllSubSubCategoriesFromSubCategory(
            @ApiParam(SUB_ID_PARAM)
            @PathParam("id")
                    Long id
    );

    @ApiOperation("Get all subsubcategories with a specified parent id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/subsubcategories/parent/{id}")
    @Deprecated
    Response deprecatedGetAllSubSubCategoriesFromParent(
            @ApiParam(SUB_ID_PARAM)
            @PathParam("id")
                    Long id
    );



    @ApiOperation("Get all categories with at least one quiz")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/withQuizzes")
    @Deprecated
    Response deprecatedGetAllCategoriesWithAtLeastOneQuiz();


    @ApiOperation("Get all subsubcategories with at least one quiz")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/withQuizzes/subsubcategories")
    @Deprecated
    Response deprecatedGetAllSubSubCategoriesWithAtLeastOneQuiz();
}
