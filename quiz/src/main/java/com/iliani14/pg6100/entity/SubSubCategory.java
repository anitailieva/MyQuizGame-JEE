package com.iliani14.pg6100.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by anitailieva on 26/10/2016.
 */

@NamedQueries({
        @NamedQuery(name = SubSubCategory.GET_ALL_SUBSUBCATEGORIES, query = "SELECT s FROM SubSubCategory s"),
        @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORY_BY_SUBCATEGORY, query = "SELECT s FROM SubSubCategory s WHERE subCategories.name = :name")
})


@Entity
public class SubSubCategory {

    public static final String GET_ALL_SUBSUBCATEGORIES = "GET ALL SUBSUBCATEGORIES";
    public static final String GET_SUBSUBCATEGORY_BY_SUBCATEGORY = "GET SUBSUBCATEGORY BY SUBCATEGORY";

    @Id @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subSubCategories")
    private List<Quiz> quizes;

    @ManyToOne
    private SubCategory subCategories;

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
