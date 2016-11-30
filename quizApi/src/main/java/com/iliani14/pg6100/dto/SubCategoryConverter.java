package com.iliani14.pg6100.dto;

import com.iliani14.pg6100.dto.collection.ListDto;
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
        dto.categoryId = String.valueOf(entity.getCategory().getId());
        dto.name = entity.getName();
        return dto;
    }

    public static ListDto<SubCategoryDto> transform(List<SubCategory> entities, int offset, int limit){

        List<SubCategoryDto> dtoList = null;
        if(entities != null){
            dtoList = entities.stream()
                    .skip(offset)
                    .limit(limit)
                    .map(SubCategoryConverter::transform)
                    .collect(Collectors.toList());

        }

        ListDto<SubCategoryDto> dto = new ListDto<>();
        dto.list = dtoList;
        dto._links = new ListDto.ListLinks();
        dto.rangeMin = offset;
        assert dtoList != null;
        dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
        dto.totalSize = entities.size();


        return dto;
    }
}
