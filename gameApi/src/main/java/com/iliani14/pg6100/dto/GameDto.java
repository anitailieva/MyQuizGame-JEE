package com.iliani14.pg6100.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by anitailieva on 20/11/2016.
 */

@ApiModel("A game")
public class GameDto {

    @ApiModelProperty("The id of the game")
    public String id;

    @ApiModelProperty("Number of questions")
    public int numberOfQuestions;

    @ApiModelProperty("Number of answers")
    public int answer;

    public GameDto() {
    }

    public GameDto(String id, int numberOfQuestions, int answer){
        this.id = id;
        this.numberOfQuestions = numberOfQuestions;
        this.answer = answer;
    }
}
