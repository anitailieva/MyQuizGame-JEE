package com.iliani14.pg6100.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by alexandershipunov on 19/11/2016.
 * Game data transfer object
 */
@ApiModel("A game")
public class GameDto {

    @ApiModelProperty("The id of the game")
    public String id;

    @ApiModelProperty("Number of answers so far")
    public int numberOfAnswers;

    @ApiModelProperty("Number of questions in the game")
    public int numberOfQuestions;

    @ApiModelProperty("Is the game active")
    public boolean isActive;

    @ApiModelProperty("URI to the current quiz")
    public String currentQuizURI;

    public GameDto() {
    }

    public GameDto(String id, int numberOfAnswers, int numberOfQuestions, String currentQuizURI, boolean isActive) {
        this.id = id;
        this.numberOfAnswers = numberOfAnswers;
        this.numberOfQuestions = numberOfQuestions;
        this.currentQuizURI = currentQuizURI;
        this.isActive = isActive;
    }
}
