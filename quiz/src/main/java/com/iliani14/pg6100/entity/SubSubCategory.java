package com.iliani14.pg6100.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@Entity
public class SubSubCategory {


    @Id
    @Size(min = 1, max = 50)
    private String id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subSubCategories")
    private List<Quiz> quizs;

    @ManyToOne
    private SubCategory subCategories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
