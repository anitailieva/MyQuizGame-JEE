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

    @ApiModelProperty("The name of the subsubcategory")
    public String name;

    @ApiModelProperty("The name of the parent subcategory")
    public String subcategoryId;

    public SubSubCategoryDto(){}

    public SubSubCategoryDto(String id, String name, String subcategoryId){
        this.id = id;
        this.name = name;
        this.subcategoryId = subcategoryId;
    }


}
