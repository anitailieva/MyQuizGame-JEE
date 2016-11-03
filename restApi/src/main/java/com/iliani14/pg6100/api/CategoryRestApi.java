package com.iliani14.pg6100.api;

import com.iliani14.pg6100.dto.CategoryDto;
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


    @ApiOperation("Get all the subcategories by category id")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/subcategories")
    List<SubCategoryDto> getSubCategoriesByCategoryId(
            @ApiParam("categoryId")
            @PathParam("id")
            Long id);


    @ApiOperation("Get a subcategory by category id and own id")
    @GET
    @Path("/{categoryId}/subcategories/{id}")
    SubCategoryDto getSubCategoryByCategoryIdAndOwnId(
            @ApiParam("Category id")
            @PathParam("categoryId")
            Long categoryId,

            @ApiParam("The of the subCategory")
            @PathParam("id")
            Long id
    );

    @ApiOperation("Get all the subcategories")
    @GET
    @Path("/subcategories")
    List<SubCategoryDto> getAllSubCategories();

    @ApiOperation("Get all the subsubcategories by category id and subcategory id")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/subcategories/{subcategoryId}/subsubcategories")
    List<SubSubCategoryDto> getSubSubCatBySubCategoryIdAndSubCategoryId(
            @ApiParam("Category Id")
            @PathParam("id")
            Long categoryId,

            @ApiParam("Subcategory Id")
            @PathParam("subcategoryId")
            Long subCategoryId
    );


    @ApiOperation("Get a subsubcategory by category id, subcategory id and own id")
    @GET
    @Path("{categoryId}/subcategories/{subcategoryId}/subsubcategories/{id}")
    SubSubCategoryDto getSubSubCategoryIdSubCategoryIdAndOwnId(
            @ApiParam("Category id")
            @PathParam("categoryId")
            Long categoryId,

            @ApiParam("Subcategory Id")
            @PathParam("subcategoryId")
            Long subcategoryId,

            @ApiParam("Subsubcategory id")
            @PathParam("id")
            Long id

    );

    @ApiOperation("Get all the subsubcategories")
    @GET
    @Path("/subsubcategories")
    List<SubSubCategoryDto> getAllSubSubCategories();

    @ApiOperation("Delete a category")
    @DELETE
    @Path("/id/{id}")
    void deleteCategory(
            @ApiParam("Category Id")
            @PathParam("id")
            Long id);


    @ApiOperation("Delete a subcategory")
    @DELETE
    @Path("/subcategories/{id}")
    void deleteSubCategory(
            @ApiParam("SubCategory Id")
            @PathParam("id")
            Long id
    );

    @ApiOperation("Delete a subsubcategory")
    @DELETE
    @Path("/subsubcategories/{id}")
    void deleteSubSubCategory(
            @ApiParam("SubSubCategory id")
            @PathParam("id")
            Long id);


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


    @ApiOperation("Update the name of en existing category")
    @PUT
    @Path("/id/{id}/name")
    @Consumes(MediaType.TEXT_PLAIN)
    void updateName(
        @ApiParam("id")
        @PathParam("id")
        Long id,

        @ApiParam("The category name which will replace the old one")
        String name
    );
}
