package com.iliani14.pg6100.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anitailieva on 20/11/2016.
 */
@Entity
public class Game {

        @Id @GeneratedValue
        private Long id;


        private int answer;

        @OneToMany(fetch = FetchType.EAGER)
        private List<Question> questions;

        public Game(){
        }

        public Long getId(){
            return id;
        }

        public void setId(Long id){
            this.id = id;
        }

        public int getAnswer(){
            return answer;
        }

        public void setAnswer(int answer){
            this.answer = answer;
        }
        public List<Question> getQuestions(){
            if(questions == null)
            return new ArrayList<>();

            return questions;
        }

        public void setQuestions(List<Question> questions){
            this.questions = questions;
        }





}
