package com.iliani14.pg6100.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@NamedQueries({
        @NamedQuery(name = SubCategory.GET_ALL_SUBATEGORIES, query = "SELECT s FROM SubCategory s"),
        @NamedQuery(name = SubCategory.GET_SUBCATEGORY_BY_CATEGORY, query = "SELECT s FROM SubCategory s WHERE category.id = :id")
})

@Entity
public class SubCategory {

    public static final String GET_ALL_SUBATEGORIES = "GET ALL SUBCATEGORIES";
    public static final String GET_SUBCATEGORY_BY_CATEGORY = "GET SUBCATEGORY BY CATEGORY";

    @Id
    @Size(min = 1, max = 300)
    private String id;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subCategories")
    private List<SubSubCategory> subSubCategories;


    @ManyToOne
    private Category category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SubSubCategory> getSubSubCategories(){
        if(subSubCategories == null){
            return new ArrayList<>();
        }


        return subSubCategories;
    }

    public void setSubSubCategories(List<SubSubCategory> subSubCategories){
        this.subSubCategories = subSubCategories;
    }
    public Category getCategory(){
        return category;
    }

    public void setCategory(Category category){
        this.category = category;
    }

}
