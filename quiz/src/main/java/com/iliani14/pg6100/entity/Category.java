package com.iliani14.pg6100.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by anitailieva on 26/10/2016.
 */
@NamedQueries({
        @NamedQuery(name = Category.GET_ALL_CATEGORIES, query = "SELECT c FROM Category c"),
        @NamedQuery(name = Category.GET_CATEGORY_BY_ID, query = "SELECT c FROM Category c WHERE c.id = :id")

})

@Entity
public class Category {

    public static final String GET_ALL_CATEGORIES = "GET ALL CATEGORIES";
    public static final String GET_CATEGORY_BY_ID = "GET CATEGORY BY ID";


    @Id
    @Size(min = 1, max = 50)
    private String id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<SubCategory> subCategories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SubCategory> getSubCategories(){
        if(subCategories == null){
            return new ArrayList<>();
        }
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories){
        this.subCategories = subCategories;
    }

}
