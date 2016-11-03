package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.entity.SubSubCategory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by anitailieva on 27/10/2016.
 */
public class SubSubCategoryConverter {

    private SubSubCategoryConverter(){}

    public static SubSubCategoryDto transform(SubSubCategory entity){
        Objects.requireNonNull(entity);

        SubSubCategoryDto dto = new SubSubCategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.subcategoryId = String.valueOf(entity.getSubCategories().getId());
        dto.name = entity.getName();

        return dto;

    }

    public static List<SubSubCategoryDto> transform(List<SubSubCategory> entities){
        Objects.requireNonNull(entities);

        return entities.stream().map(SubSubCategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
