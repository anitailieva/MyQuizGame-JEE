package com.iliani14.pg6100;

import com.iliani14.pg6100.entity.Question;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anitailieva on 20/11/2016.
 */

@NamedQuery(name = Game.GET_ALL_ACTIVE_GAMES, query = "SELECT g FROM Game g WHERE g.isActive = true")
@Entity
public class Game {

        public static final String GET_ALL_ACTIVE_GAMES =  "GET ALL ACTIVE GAMES";

        @Id @GeneratedValue
        private Long id;


        private int numberOfanswers;

        private boolean isActive;

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

        public int getNumberOfAnswers(){
            return numberOfanswers;
        }

        public void setNumberOfanswers(int numberOfanswers){
            this.numberOfanswers = numberOfanswers;
        }

        public boolean isActive(){
           setActive(questions.size() == numberOfanswers);
                return isActive;
        }
        public void setActive(boolean isActive){
            isActive = isActive;
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
