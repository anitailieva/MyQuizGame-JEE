package com.iliani14.pg6100.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

/**
 * Created by anitailieva on 26/10/2016.
 */

@Entity
public class Quiz {


    @Id
    @Size(min = 1, max = 50)
    private String id;

    @ManyToOne
    private SubSubCategory subSubCategories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
