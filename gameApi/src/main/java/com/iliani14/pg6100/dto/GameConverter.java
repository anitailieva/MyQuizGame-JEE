package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.entity.Game;

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
        dto.answer = entity.getAnswer();

        return dto;
    }

    public static List<GameDto> transform(List<Game> entities){
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(GameConverter::transform)
                .collect(Collectors.toList());
    }

}
