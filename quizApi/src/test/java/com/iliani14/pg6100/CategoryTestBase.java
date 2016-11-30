package com.iliani14.pg6100;

import com.iliani14.pg6100.dto.collection.ListDto;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Created by anitailieva on 29/10/2016.
 */
public class CategoryTestBase {

    @BeforeClass
    public static  void initClass() {
       JBossUtil.waitForJBoss(10);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/myquiz/api/category";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Before
    @After
    public void clean() {

        int total = Integer.MAX_VALUE;
        /*
           Recall, as Wildfly is running as a separated process, changed
           in the database will impact all the tests.
           Here, we read each resource (GET), and then delete them
           one by one (DELETE)
         */
        while (total > 0) {

            //seems there are some limitations when handling generics
            ListDto<?> listDto = given()
                    .queryParam("limit", Integer.MAX_VALUE)
                    .get()
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(ListDto.class);

            listDto.list.stream()
                    .map(n -> ((Map) n).get("id"))
                    .forEach(id ->
                            given().delete("/id/" + id)
                                    .then()
                                    .statusCode(204)
                    );

            total = listDto.totalSize - listDto.list.size();
        }
    }


}
