package com.iliani14.pg6100.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by anitailieva on 27/10/2016.
 */
@ApiModel("A subcategory")
public class SubCategoryDto {

    @ApiModelProperty("The id of the subcategory")
    public String id;

    @ApiModelProperty("The id of the category")
    public String categoryId;

    @ApiModelProperty("The name of the subcategory")
    public String name;

    public SubCategoryDto(){}


    public SubCategoryDto(String id, String categoryId, String name){
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }
}
