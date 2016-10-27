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

    @ApiModelProperty("The name of the subcategory")
    public String name;

    @ApiModelProperty("The name of the parent category")
    public String category;

    public SubCategoryDto(){}


    public SubCategoryDto(String id, String name, String category){
        this.id = id;
        this.name = name;
        this.category = category;
    }
}
