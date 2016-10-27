package com.iliani14.pg6100.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by anitailieva on 26/10/2016.
 */
@NamedQueries({
        @NamedQuery(name = Category.GET_ALL_CATEGORIES, query = "SELECT c FROM Category c"),
        @NamedQuery(name = Category.GET_CATEGORY_BY_NAME, query = "SELECT c FROM Category c WHERE c.name = :name")

})

@Entity
public class Category {

    public static final String GET_ALL_CATEGORIES = "GET ALL CATEGORIES";
    public static final String GET_CATEGORY_BY_NAME = "GET CATEGORY BY NAME";

    @Id @GeneratedValue
    private long id;


    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<SubCategory> subCategories;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
