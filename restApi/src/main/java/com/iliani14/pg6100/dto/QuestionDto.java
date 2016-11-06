package com.iliani14.pg6100.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by anitailieva on 06/11/2016.
 */
@ApiModel("A question")
public class QuestionDto {

    @ApiModelProperty("Id of the question")
    public String id;

    @ApiModelProperty("The parent category of the question")
    public String subSubCategoryId;

    @ApiModelProperty("The question")
    public String question;

    @ApiModelProperty("The answers for the question")
    public List<String> answersList;

    @ApiModelProperty("The correct answer")
    public String correctAnswer;


    public QuestionDto(){}

    public QuestionDto(String id, String parentId, String question, List<String> answers, String theCorrectAnswer) {
        this.id = id;
        this.subSubCategoryId = parentId;
        this.question = question;
        answersList = answers;
        this.correctAnswer = theCorrectAnswer;

    }
}
