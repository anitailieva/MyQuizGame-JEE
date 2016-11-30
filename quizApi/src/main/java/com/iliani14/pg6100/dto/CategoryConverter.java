package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.dto.collection.ListDto;
import com.iliani14.pg6100.entity.Category;
import com.iliani14.pg6100.entity.SubCategory;
import com.iliani14.pg6100.entity.SubSubCategory;

import java.util.ArrayList;
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

    public static CategoryDto transform(Category category, boolean expand) {
        Objects.requireNonNull(category);

        CategoryDto dto = new CategoryDto();
        dto.id = String.valueOf(category.getId());
        dto.name = category.getName();

        if(expand) {
            dto.subcategories = new ArrayList<>();
            category.getSubCategories().stream()
                    .map(SubCategoryConverter::transform)
                    .forEach(subCategoryDto -> dto.subcategories.add(subCategoryDto));

            dto.subSubCategories = new ArrayList<>();

            List<SubCategory> subCategories = new ArrayList<>(category.getSubCategories());
            for(SubCategory s : subCategories) {
                for(SubSubCategory ssb : s.getSubSubCategories()){
                    dto.subSubCategories.add(SubSubCategoryConverter.transform(ssb));

                }
            }
        }

        return dto;
    }

    public static ListDto<CategoryDto> transform(List<Category> categories, int offset,
                                                 int limit, boolean expand){

        List<CategoryDto> dtoList = null;
        if(categories != null){
            dtoList = categories.stream()
                    .skip(offset) // this is a good example of how streams simplify coding
                    .limit(limit)
                    .map(category -> transform(category, expand))
                    .collect(Collectors.toList());
        }

        ListDto<CategoryDto> dto = new ListDto<>();
        dto.list = dtoList;
        dto._links = new ListDto.ListLinks();
        dto.rangeMin = offset;
        assert dtoList != null;
        dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
        dto.totalSize = categories.size();

        return dto;
    }
}
