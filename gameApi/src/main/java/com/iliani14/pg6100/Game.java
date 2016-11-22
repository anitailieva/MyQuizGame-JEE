package com.iliani14.pg6100;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anitailieva on 20/11/2016.
 */
@NamedQueries({
        @NamedQuery(name = Game.GET_ALL_ACTIVE_GAMES, query = "select g from Game g where isActive = TRUE"),
})

@Entity
public class Game {

        public static final String GET_ALL_ACTIVE_GAMES =  "GET ALL ACTIVE GAMES";

        @Id @GeneratedValue
        private Long id;


        private int numberOfanswers;

        private boolean isActive;

        @ElementCollection(fetch = FetchType.EAGER)
        private List<Long> questions;

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

        public List<Long> getQuestions(){
            if(questions == null)
            return new ArrayList<>();

            return questions;
        }

        public void setQuestions(List<Long> questions){
            this.questions = questions;
        }





}
