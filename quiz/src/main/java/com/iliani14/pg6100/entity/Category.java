package com.iliani14.pg6100.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by anitailieva on 26/10/2016.
 */

@Entity
public class Category {

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
