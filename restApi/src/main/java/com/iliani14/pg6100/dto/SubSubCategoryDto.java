package com.iliani14.pg6100.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by anitailieva on 27/10/2016.
 */
@ApiModel("A subsubcategory")
public class SubSubCategoryDto {

    @ApiModelProperty("The id of the subsubcategory")
    public String id;


    @ApiModelProperty("The id of the subcategory subcategory")
    public String subcategoryId;


    @ApiModelProperty("The name of the subsubcategory")
    public String name;

    public SubSubCategoryDto(){}

    public SubSubCategoryDto(String id, String subcategoryId, String name) {
        this.id = id;
        this.subcategoryId = subcategoryId;
        this.name = name;
    }


}
