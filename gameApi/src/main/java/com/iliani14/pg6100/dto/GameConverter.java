package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.Game;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by anitailieva on 20/11/2016.
 */
public class GameConverter {

    private GameConverter(){};

    public static GameDto transform(Game entity){
        Objects.requireNonNull(entity);

        GameDto dto = new GameDto();
        dto.id = String.valueOf(entity.getId());
        dto.numberOfQuestions = entity.getQuestions().size();
        dto.numberOfAnswers = entity.getNumberOfAnswers();
        dto.isActive = entity.isActive();

        if(entity.isActive()){
            dto.uri = "http://<...>/quizzes/{id}" +  entity.getQuestions().get(entity.getNumberOfAnswers());
        } else {
            dto.uri = "No uri available, the game is not active.";
        }

        return dto;
    }

    public static List<GameDto> transform(List<Game> entities){
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(GameConverter::transform)
                .collect(Collectors.toList());
    }

}
