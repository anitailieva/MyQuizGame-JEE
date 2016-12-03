package com.iliani14.pg6100;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.iliani14.pg6100.api.Formats;
import com.iliani14.pg6100.dto.collection.ListDTO;
import io.restassured.RestAssured;
import org.junit.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;

/**
 * Created by anitailieva on 03/12/2016.
 */
public class GameApplicationTestBase  {

    private static WireMockServer wireMockServer;

    static {
        System.setProperty("quizApiAddress", "localhost:8099");
    }

    @BeforeClass
    public static void initRestAssured() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/game/api/games";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        wireMockServer = new WireMockServer(
                wireMockConfig().port(8099).notifier(new ConsoleNotifier(true))
        );
        wireMockServer.start();
    }

    @Before
    @After
    public void clean() {

        int total = Integer.MAX_VALUE;

        while (total > 0) {

            ListDTO<?> listDto = given()
                    .queryParam("limit", Integer.MAX_VALUE)
                    .get()
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(ListDTO.class);

            listDto.list.stream()
                    .map(n -> ((Map) n).get("id"))
                    .forEach(id ->
                            given().delete("/" + id)
                                    .then()
                                    .statusCode(204)
                    );

            total = listDto.totalSize - listDto.list.size();
        }
    }

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testCreateAndGetGame() throws UnsupportedEncodingException {

        String jsonWithQuizIds = "{\"ids\":[1, 2, 3, 4, 5]}";
        int limit = 5;

        given().contentType(Formats.V1_JSON)
                .get()
                .then()
                .statusCode(200)
                .body("list.size", is(0));

        String id = createGame(jsonWithQuizIds, limit);

        given().contentType(Formats.V1_JSON)
                .get("/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("numberOfQuestions", is(limit));
    }

    @Test
    public void testDeleteAGame() throws UnsupportedEncodingException {
        String jsonWithQuizIds = "{\"ids\":[1, 2, 3, 4, 5]}";
        int limit = 5;

        get().then().statusCode(200).body("list.size", is(0));

        String id = createGame(jsonWithQuizIds, limit);

        given().contentType(Formats.V1_JSON)
                .get("/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("numberOfQuestions", is(limit));

        delete("/" + id);

        get().then().statusCode(200).body("list.size", is(0));
    }


    private String createGame(String jsonWithQuizIds, int limit) throws UnsupportedEncodingException {

        wireMockServer.stubFor(
                WireMock.post(
                        urlMatching(".*randomQuizzes.*"))
                        .withQueryParam("limit", WireMock.matching("\\d+"))
                        .withQueryParam("filter", WireMock.matching("ss_" + "\\d+"))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + jsonWithQuizIds.getBytes("utf-8").length)
                                .withBody(jsonWithQuizIds)));

        return given().queryParam("limit", limit)
                .post()
                .then()
                .statusCode(200).extract().asString();
    }

}
