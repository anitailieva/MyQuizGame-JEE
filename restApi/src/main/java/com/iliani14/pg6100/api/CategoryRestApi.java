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
            @ApiParam("Id of the subcategory, name and category id")
                    SubCategoryDto dto);

    @ApiOperation("Create a subsubcategory")
    @POST
    @Path("/subcategories/subsubcategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created subsubcategory")
    Long createSubSubCategory(
            @ApiParam("Id of the subsubcategory, name and subcategory id")
                    SubSubCategoryDto dto);


    @ApiOperation("Get all the categories")
    @GET
    List<CategoryDto> get();


    @ApiOperation("Get a category by id")
    @GET
    @Path("id/{id}")
    CategoryDto getCategoryById(
            @ApiParam("The id of the category")
            @PathParam("id")
            Long id);


    @ApiOperation("Get all the subcategories")
    @GET
    @Path("/{id}/subcategories")
    List<SubCategoryDto> getSubCategories();


    @ApiOperation("Get a subcategory")
    @GET
    @Path("/{category_id}/subcategories/{id}")
    SubCategoryDto getSubCategoryById(
            @ApiParam("SubCategory id")
            @PathParam("id")
            Long subId
    );

    @ApiOperation("Get all the subsubcategories")
    @GET
    @Path("/{id}/subsubcategories")
    List<SubSubCategoryDto> getSubSubCategories();


    @ApiOperation("Get a subsubcategory")
    @GET
    @Path("/{subcategory_id}/subsubcategories/{id}")
    SubSubCategoryDto getSubSubCategoryId(
            @ApiParam("SubSubCategory id")
            @PathParam("id")
            Long id

    );


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
}
