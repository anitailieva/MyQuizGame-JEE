
package com.iliani14.pg6100;

import com.iliani14.pg6100.dto.CategoryDto;
import com.iliani14.pg6100.dto.QuestionDto;
import com.iliani14.pg6100.dto.SubCategoryDto;
import com.iliani14.pg6100.dto.SubSubCategoryDto;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;


/**
 * Created by anitailieva on 28/10/2016.
 */
public class CategoryIT extends CategoryTestBase {

    private String createCategory(String name) {
        return given().contentType(ContentType.JSON)
                .body(new CategoryDto(null, name))
                .post()
                .then()
                .statusCode(200)
                .extract().asString();
    }


    private String createParentCategory() {
        return createCategory("The parent category");
    }

    private String createSubCategory(String categoryId, String name) {
        return given().contentType(ContentType.JSON)
                .body(new SubCategoryDto(null, categoryId, name))
                .post("/subcategories")
                .then()
                .statusCode(200)
                .extract().asString();
    }

    private String createSubSubCategory(String subCategoryId, String name) {
        return given().contentType(ContentType.JSON)
                .body(new SubSubCategoryDto(null, subCategoryId, name))
                .post("/subsubcategories")
                .then()
                .statusCode(200)
                .extract().asString();
    }

    private String createQuestion(String subsubcategoryId, String question, List<String> answers, String correctAnswer) {
        return given().contentType(ContentType.JSON)
                .body(new QuestionDto(null, subsubcategoryId, question, answers, correctAnswer))
                .post("/questions")
                .then()
                .statusCode(200)
                .extract().asString();
    }

    @Test
    public void testCleanDB() {
        get().then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testGetAllCategories() {
        get().then().body("size()", is(0));

        String category1 = createCategory("Science");
        String category2 = createCategory("Sports");
        String category3 = createCategory("History");


        get().then().body("size()", is(3));

        given().get()
                .then()
                .statusCode(200)
                .body("id", hasItems(category1, category2, category3))
                .body("name", hasItems("Science", "Sports", "History"));

    }

    @Test
    public void testCreateAndGetCategory() {
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

    @Test
    public void testDeleteCategory() {
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
    public void testGetAllSubcategories() {
        get("/subcategories").then().body("size()", is(0));

        String category = createCategory("Science");

        String subcategory1 = createSubCategory(category, "Subcategory1");
        String subcategory2 = createSubCategory(category, "Subcategory2");
        String subcategory3 = createSubCategory(category, "Subcategory3");

        get("/subcategories").then().body("size()", is(3));

        given().get("/subcategories")
                .then()
                .statusCode(200)
                .body("id", hasItems(subcategory1, subcategory2, subcategory3))
                .body("name", hasItems("Subcategory1", "Subcategory2", "Subcategory3"));

    }


    @Test
    public void testCreateAndGetSubCategory() {
        String category = createParentCategory();
        String subcategoryName = "Subcategory name";

        SubCategoryDto dto = new SubCategoryDto(null, category, subcategoryName);

        get("/subcategories").then().body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post("/subcategories")
                .then()
                .statusCode(200)
                .extract().asString();

        get("/subcategories").then().body("size()", is(1));

        given().pathParam("id", id)
                .get("/subcategories/id/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is(subcategoryName));

    }

    @Test
    public void testDeleteSubcategory() {
        String id = given().contentType(ContentType.JSON)
                .body(new SubCategoryDto(null, createParentCategory(), "SUB name"))
                .post("/subcategories")
                .then()
                .statusCode(200)
                .extract().asString();

        get("/subcategories").then().body("id", contains(id));

        delete("/subcategories/id/" + id);

        get("/subcategories").then().body("id", not(contains(id)));


    }

    @Test
    public void updateSubcategory() throws Exception {
        String name = "Name";

        String id = given().contentType(ContentType.JSON)
                .body(new SubCategoryDto(null, createParentCategory(), name))
                .post("/subcategories")
                .then()
                .statusCode(200)
                .extract().asString();

        get("/subcategories/id/" + id).then().body("name", is(name));


        String newName = "New name";

        given().contentType(ContentType.TEXT)
                .body(newName)
                .pathParam("id", id)
                .put("/subcategories/id/{id}/name")
                .then()
                .statusCode(204);

        get("/subcategories/id/" + id).then().body("name", is(newName));

    }

    @Test
    public void testGetAllSubSubcategories() {
        String category = createCategory("Science");

        String subcategory1 = createSubCategory(category, "Subcategory1");
        String subcategory2 = createSubCategory(category, "Subcategory2");
        String subcategory3 = createSubCategory(category, "Subcategory3");

        String subsubcategory1 = createSubSubCategory(subcategory1, "Subsubcategory1");
        String subsubcategory2 = createSubSubCategory(subcategory2, "Subsubcategory2");
        String subsubcategory3 = createSubSubCategory(subcategory3, "Subsubcategory3");
        String subsubcategory4 = createSubSubCategory(subcategory1, "Subsubcategory1");


        get("/subsubcategories").then().body("size()", is(4));

        given().get("/subsubcategories")
                .then()
                .statusCode(200)
                .body("id", hasItems(subsubcategory1, subsubcategory2, subsubcategory3, subsubcategory4))
                .body("name", hasItems("Subsubcategory1", "Subsubcategory2", "Subsubcategory3", "Subsubcategory1"));

    }

    @Test
    public void testCreateAndGetSubSubCategory() {
        String category = createCategory("Science");
        String subcategory = createSubCategory(category, "Computer Science");

        String name = "Java";

        SubSubCategoryDto dto = new SubSubCategoryDto(null, subcategory, name);

        get("subsubcategories").then().statusCode(200).body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post("/subsubcategories")
                .then()
                .statusCode(200)
                .extract().asString();

        get("/subsubcategories").then().statusCode(200).body("size()", is(1));

        given().pathParam("id", id)
                .get("/subsubcategories/id/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is(name));
    }

    @Test
    public void testDeleteSubSubCategory() {
        String id = given().contentType(ContentType.JSON)
                .body(new SubSubCategoryDto(null, createSubCategory(createParentCategory(), "SubCategory name"), "SubSubcategory name"))
                .post("/subsubcategories")
                .then()
                .statusCode(200)
                .extract().asString();

        get("/subsubcategories").then().body("id", contains(id));

        delete("/subsubcategories/id/" + id);

        get("/subsubcategories").then().body("id", not(contains(id)));

    }

    @Test
    public void testUpdateSubSubCategoryName() {
        String name = "Subsubcategory name";

        String id = given().contentType(ContentType.JSON)
                .body(new SubSubCategoryDto(null, createSubCategory(createParentCategory(), "Subcategory name"), name))
                .post("/subsubcategories")
                .then()
                .statusCode(200)
                .extract().asString();

        get("/subsubcategories/id/" + id).then().body("name", is(name));

        String newName = "New name";

        given().contentType(ContentType.TEXT)
                .body(newName)
                .pathParam("id", id)
                .put("/subsubcategories/id/{id}/name")
                .then()
                .statusCode(204);

        get("/subsubcategories/id/" + id).then().body("name", is(newName));
    }


    @Test
    public void testGetAllQuestions() {
        String category = createCategory("Science");
        String subcategory1 = createSubCategory(category, "Subcategory1");
        String subsubcategory1 = createSubSubCategory(subcategory1, "Subsubcategory1");


        List<String> answers = new ArrayList<>();
        answers.add("one");
        answers.add("two");
        answers.add("three");
        answers.add("four");
        String theCorrectAnswer1 = answers.get(2);
        String theCorrectAnswer2 = answers.get(3);
        String theCorrectAnswer3 = answers.get(0);

        String question1 = createQuestion(subsubcategory1, "Question1", answers, theCorrectAnswer1);
        String question2 = createQuestion(subsubcategory1, "Question2", answers, theCorrectAnswer2);
        String question3 = createQuestion(subsubcategory1, "Question3", answers, theCorrectAnswer3);


        get("/questions").then().body("size()", is(3));

        given().get("/questions")
                .then()
                .statusCode(200)
                .body("id", hasItems(question1, question2, question3))
                .body("question", hasItems("Question1", "Question2", "Question3"));

    }


    @Test
    public void testCreateAndGetQuestion() {
        String category = createCategory("Science");
        String subcategory = createSubCategory(category, "Subcategory");
        String subsubcategory = createSubSubCategory(subcategory, "Subsubcategory");

        String question = "Question";

        List<String> answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        answers.add("4");
        String theCorrectAnswer = answers.get(1);

        QuestionDto dto = new QuestionDto(null, subsubcategory, question, answers, theCorrectAnswer);

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post("/questions")
                .then()
                .statusCode(200)
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));


        given().pathParam("id", id)
                .get("/questions/id/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("subSubCategoryId", is(subsubcategory))
                .body("question", is(question))
                .body("answers", is(answers))
                .body("theCorrectAnswer", is(theCorrectAnswer));
    }


    @Test
    public void testDeleteQuestion() {
        List<String> answers = new ArrayList<>();
        answers.add("one");
        answers.add("two");
        answers.add("three");
        answers.add("four");

        String id = given().contentType(ContentType.JSON)
                .body(new QuestionDto(null, createSubSubCategory(createSubCategory(createCategory("Science"), "Computer Science"),
                        "Java EE"), "Question", answers, "four"))
                .post("/questions")
                .then()
                .statusCode(200)
                .extract().asString();

        get("/questions").then().body("id", contains(id));

        delete("/questions/id/" + id);

        get("/questions").then().body("id", not(contains(id)));


    }

    @Test
    public void testUpdateQuestion() {
        List<String> answers = new ArrayList<>();
        answers.add("one");
        answers.add("two");
        answers.add("three");
        answers.add("four");

        String question = "Question";
        String id = given().contentType(ContentType.JSON)
                .body(new QuestionDto(null, createSubSubCategory(createSubCategory(createCategory("Science"), "Computer Science"),
                        "Java EE"), question, answers, "four"))
                .post("/questions")
                .then()
                .statusCode(200)
                .extract().asString();


        get("/questions/id/" + id).then().body("question", is(question));

        String newQuestion = "New question";

        given().contentType(ContentType.TEXT)
                .body(newQuestion)
                .pathParam("id", id)
                .put("/questions/id/{id}/question")
                .then()
                .statusCode(204);

        get("/questions/id/" + id).then().body("question", is(newQuestion));

    }

}