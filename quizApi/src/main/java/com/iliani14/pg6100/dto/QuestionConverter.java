package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.dto.collection.ListDto;
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

    public static ListDto<QuestionDto> transform(List<Question> questions, int offset, int limit) {

        List<QuestionDto> dtoList = null;
        if (questions != null) {
            dtoList = questions.stream()
                    .skip(offset)
                    .limit(limit)
                    .map(QuestionConverter::transform)
                    .collect(Collectors.toList());
        }

        ListDto<QuestionDto> dto = new ListDto<>();
            dto.list = dtoList;
            dto._links = new ListDto.ListLinks();
            dto.rangeMin = offset;
            dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
            dto.totalSize = questions.size();

            return dto;

    }


}
