package com.iliani14.pg6100.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by anitailieva on 27/10/2016.
 */


/*
    A data transfer object (DTO) is what we will use to represent and (un)marshal
     JSon objects.
     Note: it is perfectly fine that fields here are all "public".
     This is just a POJO (plain old Java object), with no logic, just data.
     Also note how Swagger is used here to provide documentation.
 */
@ApiModel("A category")
public class CategoryDto {

    @ApiModelProperty("The id of the category")
    public String id;

    @ApiModelProperty("The name of the category")
    public String name;


    @ApiModelProperty("Subcategories that belong to the category")
    public List<SubCategoryDto> subcategories;

    @ApiModelProperty("Specifying categories that belong to the category")
    public List<SubSubCategoryDto> subSubCategories;

    public CategoryDto() {
    }

    public CategoryDto(String id, String name, List<SubCategoryDto> subcategories,
                    List<SubSubCategoryDto> subSubCategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
        this.subSubCategories = subSubCategories;
    }
}
