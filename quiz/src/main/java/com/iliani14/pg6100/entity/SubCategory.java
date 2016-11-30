package com.iliani14.pg6100.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anitailieva on 26/10/2016.
 */

@NamedQueries({
        @NamedQuery(name = SubCategory.GET_ALL_SUBCATEGORIES, query = "SELECT s FROM SubCategory s"),
        @NamedQuery(name = SubCategory.GET_SUBCATEGORY_BY_CATEGORY, query = "SELECT s FROM SubCategory s WHERE s.category.name = ?1")
})

@Entity
public class SubCategory {

    public static final String GET_ALL_SUBCATEGORIES = "GET ALL SUBCATEGORIES";
    public static final String GET_SUBCATEGORY_BY_CATEGORY = "GET SUBCATEGORY BY CATEGORY";

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 1, max = 300)
    private String name;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "subCategories")
    private List<SubSubCategory> subSubCategories;


    @ManyToOne
    private Category category;


    public List<SubSubCategory> getSubSubCategories() {
        if (subSubCategories == null) {
            return new ArrayList<>();
        }


        return subSubCategories;
    }

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


    public void setSubSubCategories(List<SubSubCategory> subSubCategories) {
        this.subSubCategories = subSubCategories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public List<Question> getListOfQuestions() {
        List<Question> questions = new ArrayList<>();
        for (SubSubCategory s : getSubSubCategories()) {
            questions = Stream.concat(questions.stream(), s.getQuestions().stream()).collect(Collectors.toList());
        }
        return questions;

    }

}
