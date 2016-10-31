package com.iliani14.pg6100;

import com.iliani14.pg6100.dto.CategoryDto;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;


/**
 * Created by anitailieva on 28/10/2016.
 */
public class CategoryIT extends CategoryTestBase {

    @Test
    public void testCleanDB() {
        get().then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testGetAllCategories() {
        get().then().body("size()", is(0));

        createSomeCategories();

        get().then().body("size()", is(4));

    }
    private void createSomeCategories(){
        createCategories("Science");
        createCategories("Sports");
        createCategories("History");
        createCategories("Music");
    }


    private void createCategories(String name){
        given().contentType(ContentType.JSON)
                .body(new CategoryDto(null, name))
                .post()
                .then()
                .statusCode(200);

    }



}
