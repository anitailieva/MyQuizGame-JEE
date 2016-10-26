package com.iliani14.pg6100.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@NamedQueries({
        @NamedQuery(name = SubSubCategory.GET_ALL_SUBSUBCATEGORIES, query = "SELECT s FROM SubSubCategory s"),
        @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORY_BY_SUBCATEGORY, query = "SELECT s FROM SubSubCategory s WHERE subCategories.id = :id")
})


@Entity
public class SubSubCategory {

    public static final String GET_ALL_SUBSUBCATEGORIES = "GET ALL SUBSUBCATEGORIES";
    public static final String GET_SUBSUBCATEGORY_BY_SUBCATEGORY = "GET SUBSUBCATEGORY BY SUBCATEGORY";

    @Id
    @Size(min = 1, max = 50)
    private String id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subSubCategories")
    private List<Quiz> quizes;

    @ManyToOne
    private SubCategory subCategories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubCategory getSubCategories(){
        return subCategories;
    }
    public void setSubCategories(SubCategory subCategories){
        this.subCategories = subCategories;
    }

    public List<Quiz> getQuizes(){
        return quizes;
    }

    public void setQuizes(List<Quiz> quizes){
        this.quizes = quizes;
    }
}
