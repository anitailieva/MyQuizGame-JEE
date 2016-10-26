package com.iliani14.pg6100.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@NamedQueries({
        @NamedQuery(name = Quiz.GET_ALL_QUIZES, query = "SELECT q FROM Quiz q"),
        @NamedQuery(name = Quiz.GET_QUIZ_BY_SUBSUBCATEGORY, query = "SELECT q FROM Quiz q WHERE q.subSubCategories.name = :name")
})


@Entity
public class Quiz {

        public static final String GET_ALL_QUIZES = "GET ALL QUIZES";
        public static final String GET_QUIZ_BY_SUBSUBCATEGORY = "GET QUIZ BY SUBSUBCATEGORY";


    @Id @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 1, max = 400)
    private String question;

    @NotNull
    @Size(min = 1, max = 400)
    private String theCorrectAnswer;


    @ElementCollection
    private List<String> answers;

    @ManyToOne
    private SubSubCategory subSubCategories;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SubSubCategory getSubSubCategories(){
        return subSubCategories;
    }
    public void setSubSubCategories(SubSubCategory subSubCategories){
        this.subSubCategories = subSubCategories;
    }

    public String getQuestion(){
        return question;
    }
    public void setQuestion(String question){
        this.question = question;
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
