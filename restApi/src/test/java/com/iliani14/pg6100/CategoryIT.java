package com.iliani14.pg6100;

import com.iliani14.pg6100.dto.CategoryDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.core.Is.is;


/**
 * Created by anitailieva on 28/10/2016.
 */
public class CategoryIT{


    @BeforeClass
    public static  void initClass() {
        JBossUtil.waitForJBoss(10);

        //RestAssured configs shared by all tests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/myquiz/api/category";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @Test
    public void testGetAllCategories() {
        RestAssured.get().then().body("size()", is(0));

        createSomeCategories();

        RestAssured.get().then().body("size()", is(4));

    }
    private void createSomeCategories(){
        createCategories("Science");
        createCategories("Sports");
        createCategories("History");
        createCategories("Music");
    }


    private void createCategories(String name){
        RestAssured.given().contentType(ContentType.JSON)
                .body(new CategoryDto(null, name))
                .post()
                .then()
                .statusCode(200);

    }



}
