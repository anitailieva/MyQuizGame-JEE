package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.dto.collection.ListDto;
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

    public static ListDto<SubSubCategoryDto> transform(List<SubSubCategory> entities, int offset, int limit){

       List<SubSubCategoryDto> dtoList = null;
       if(entities != null){
           dtoList = entities.stream()
                   .skip(offset)
                   .limit(limit)
                   .map(SubSubCategoryConverter::transform)
                   .collect(Collectors.toList());
       }

       ListDto<SubSubCategoryDto> dto = new ListDto<>();
       dto.list = dtoList;
       dto._links = new ListDto.ListLinks();
       dto.rangeMin = offset;
       assert dtoList != null;
       dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
       dto.totalSize = entities.size();

       return dto;
    }
}
