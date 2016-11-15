package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.entity.Question;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by anitailieva on 06/11/2016.
 */
public class QuestionConverter {

    private QuestionConverter() {}

    public static QuestionDto transform(Question entity) {
        Objects.requireNonNull(entity);

        QuestionDto dto = new QuestionDto();
        dto.id = String.valueOf(entity.getId());
        dto.subSubCategoryId = String.valueOf(entity.getSubSubCategories().getId());
        dto.question = entity.getQuestion();
        dto.answers= entity.getAnswers();
        dto.theCorrectAnswer = entity.getTheCorrectAnswer();

        return dto;
    }

    public static List<QuestionDto> transform(List<Question> questions){
        Objects.requireNonNull(questions);

        return questions.stream()
                .map(QuestionConverter::transform)
                .collect(Collectors.toList());
    }
}
