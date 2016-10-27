package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.entity.SubCategory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by anitailieva on 27/10/2016.
 */
public class SubCategoryConverter {

    private SubCategoryConverter(){}

    public static SubCategoryDto transform(SubCategory entity){
        Objects.requireNonNull(entity);

        SubCategoryDto dto = new SubCategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.name = entity.getName();
        dto.category = entity.getCategory().getName();
        return dto;
    }

    public static List<SubCategoryDto> transform(List<SubCategory> entities){
        Objects.requireNonNull(entities);

        return entities.stream().map(SubCategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
