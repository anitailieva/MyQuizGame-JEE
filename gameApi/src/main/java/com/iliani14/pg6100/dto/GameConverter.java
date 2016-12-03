package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.Game;
import com.iliani14.pg6100.dto.collection.ListDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class GameConverter {

    private GameConverter(){}

    public static GameDto transform(Game entity){
        Objects.requireNonNull(entity);

        GameDto dto = new GameDto();
        dto.id = String.valueOf(entity.getId());

        dto.numberOfAnswers = entity.getNumberOfAnswers();
        dto.numberOfQuestions = entity.getQuestions().size();
        dto.isActive = entity.isActive();
        if(!entity.isActive()){
            dto.currentQuizURI = "not applicable";
        } else {
            dto.currentQuizURI = "/quizApi/quizzes/" + entity.getQuestions().get(entity.getNumberOfAnswers());
        }

        return dto;
    }

    public static ListDTO<GameDto> transform(List<Game> games, int offset,
                                             int limit){
        List<GameDto> dtoList = null;
        if(games != null){
            dtoList = games.stream()
                    .skip(offset)
                    .limit(limit)
                    .map(GameConverter::transform)
                    .collect(Collectors.toList());
        }

        ListDTO<GameDto> dto = new ListDTO<>();
        dto.list = dtoList;
        dto._links = new ListDTO.ListLinks();
        dto.rangeMin = offset;
        assert dtoList != null;
        dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
        dto.totalSize = games.size();

        return dto;
    }
}
