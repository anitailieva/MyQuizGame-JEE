package com.iliani14.pg6100.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@NamedQueries({
        @NamedQuery(name = Question.GET_ALL_QUESTIONS, query = "SELECT q FROM Question q"),
        @NamedQuery(name = Question.GET_QUESTION_BY_SUBSUBCATEGORY, query = "SELECT q FROM Question q WHERE q.subSubCategories.name = :name")
})


@Entity
public class Question {

        public static final String GET_ALL_QUESTIONS = "GET ALL QUESTIONS";
        public static final String GET_QUESTION_BY_SUBSUBCATEGORY = "GET QUESTION BY SUBSUBCATEGORY";


    @Id @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 1, max = 400)
    private String text;

    @NotNull
    @Size(min = 1, max = 400)
    private String theCorrectAnswer;


    @ElementCollection
    private List<String> answers;

    @ManyToOne
    private SubSubCategory subSubCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubSubCategory getSubSubCategories(){
        return subSubCategories;
    }
    public void setSubSubCategories(SubSubCategory subSubCategories){
        this.subSubCategories = subSubCategories;
    }

    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
    }

    public String getTheCorrectAnswer(){
        return theCorrectAnswer;
    }

    public void setTheCorrectAnswer(String theCorrectAnswer){
            this.theCorrectAnswer = theCorrectAnswer;
}
    public void setAnswers(List<String> answers){
        this.answers = answers;
        }

        public List<String> getAnswers(){
            if( answers == null){
                return new ArrayList<>();
            }
            return answers;
    }

}
