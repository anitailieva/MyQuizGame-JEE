package com.iliani14.pg6100;

import com.iliani14.pg6100.dto.CategoryDto;
import com.iliani14.pg6100.dto.SubCategoryDto;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
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

    @Test
    public void testCreateAndGet(){
    String name = "Science";
        CategoryDto dto = new CategoryDto(null, name);

        get().then().statusCode(200).body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is(name));
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
    @Test
    public void testDeleteCategory(){
        String id = given().contentType(ContentType.JSON)
                .body(new CategoryDto(null, "Movies"))
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        get().then().body("id", contains(id));
        delete("/id/" + id);

        get().then().body("id", not(contains(id)));
    }

    @Test
    public void testUpdateCategory() throws Exception {

        String name = "name";

        //first create with a POST
        String id = given().contentType(ContentType.JSON)
                .body(new CategoryDto(null, name))
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        //check if POST was fine
        get("/id/" + id).then().body("name", is(name));

        String updatedName = "updated name";

        //now change name with PUT
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(new CategoryDto(id, "updated name"))
                .put("/id/{id}")
                .then()
                .statusCode(204);

        get("/id/" + id).then().body("name", is(updatedName));

        String anotherName = "another name";

        given().contentType(ContentType.TEXT)
                .body(anotherName)
                .pathParam("id", id)
                .put("/id/{id}/name")
                .then()
                .statusCode(204);

        get("/id/" + id).then().body("name", is(anotherName));
    }
    @Test
    public void testCreateAndGetSubCategories() {
        String name = "Sports";
        CategoryDto dto = new CategoryDto(null, name);

        get().then().statusCode(200).body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));


        String subName = "Computer Science";
        given().contentType(ContentType.JSON)
                .body(new SubCategoryDto(null, id, subName ))
                .post("/subcategories")
                .then()
                .statusCode(200);

        get(id + "/subcategories").then().body("size()", is(1));
    }
}
