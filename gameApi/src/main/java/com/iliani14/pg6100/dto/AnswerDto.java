package com.iliani14.pg6100.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by anitailieva on 22/11/2016.
 */
@ApiModel("The answer to the question")
public class AnswerDto {

    @ApiModelProperty("If answer is correct")
    public boolean correct;

    public AnswerDto() {
    }

    public AnswerDto(boolean correct) {
        this.correct = correct;
    }
}

