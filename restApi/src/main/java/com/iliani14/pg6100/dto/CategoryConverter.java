package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.entity.Category;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by anitailieva on 27/10/2016.
 */
/*
    Here we need a converter from @Entity to DTO
 */

public class CategoryConverter {

    private CategoryConverter(){}


    public static CategoryDto transform(Category entity){
        Objects.requireNonNull(entity);

        CategoryDto dto = new CategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.name = entity.getName();

        return dto;
    }

    public static List<CategoryDto> transform(List<Category> entities){
        Objects.requireNonNull(entities);

        return entities.stream().map(CategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
