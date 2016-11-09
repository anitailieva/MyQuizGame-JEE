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
        @NamedQuery(name = SubSubCategory.GET_ALL_SUBSUBCATEGORIES, query = "SELECT s FROM SubSubCategory s"),
        @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORY_BY_SUBCATEGORY, query = "SELECT s FROM SubSubCategory s WHERE s.subCategories.name = ?1")
})


@Entity
public class SubSubCategory {

    public static final String GET_ALL_SUBSUBCATEGORIES = "GET ALL SUBSUBCATEGORIES";
    public static final String GET_SUBSUBCATEGORY_BY_SUBCATEGORY = "GET SUBSUBCATEGORY BY SUBCATEGORY";

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subSubCategories", orphanRemoval = true)
    private List<Question> questions;

    @ManyToOne
    private SubCategory subCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public SubCategory getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(SubCategory subCategories) {
        this.subCategories = subCategories;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getListOfQuestions() {
        return new ArrayList<>(getQuestions());
    }

}
