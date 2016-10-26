package com.iliani14.pg6100.entity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@Entity
public class Quiz {


    @Id
    @Size(min = 1, max = 50)
    private String id;

    @NotNull
    @Size(min = 1, max = 400)
    private String question;

    @NotNull
    @Size(min = 1, max = 400)
    private String answer;


    @ElementCollection
    private List<String> answers;

    @ManyToOne
    private SubSubCategory subSubCategories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String answer){
            this.answer = answer;
}
    public void setAnswer(List<String> answers){
        this.answers = answers;
        }

        public List<String> getAnswers(){
            if( answers == null){
                return new ArrayList<>();
            }
            return answers;
    }

}
