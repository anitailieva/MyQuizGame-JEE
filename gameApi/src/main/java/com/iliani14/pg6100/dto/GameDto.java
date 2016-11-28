package com.iliani14.pg6100.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by anitailieva on 20/11/2016.
 */

@ApiModel("A game")
public class GameDto {

    @ApiModelProperty("The id of the game")
    public String id;

    @ApiModelProperty("The number of questions")
    public int numberOfQuestions;

    @ApiModelProperty("The number of answers")
    public int numberOfAnswers;

    @ApiModelProperty("The game is active")
    public boolean isActive;

    @ApiModelProperty("The uri of the quiz")
    public String uri;


    public List<Long> questionIds;



    public GameDto() {
    }

    public GameDto(String id, int numberOfQuestions, int numberOfAnswers, boolean isActive, String uri){
        this.id = id;
        this.numberOfQuestions = numberOfQuestions;
        this.numberOfAnswers = numberOfAnswers;
        this.isActive = isActive;
        this.uri = uri;
    }
}
