package com.iliani14.pg6100.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by anitailieva on 02/12/2016.
 */
@ApiModel("ids of the quizzes")
public class IdDto {

    @ApiModelProperty("ids")
    public List<Long> ids;

    public IdDto() {}
}

