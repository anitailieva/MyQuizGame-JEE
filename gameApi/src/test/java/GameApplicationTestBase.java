import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.iliani14.pg6100.dto.GameDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

/**
 * Created by anitailieva on 24/11/2016.
 */
public class GameApplicationTestBase {

    private static WireMockServer wireMockServer;

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

        List<GameDto> list = Arrays.asList(given().accept(ContentType.JSON).get()
                .then()
                .statusCode(200)
                .extract().as(GameDto[].class));

        list.forEach(dto ->
                given().pathParam("id", dto.id)
                        .delete("/{id}")
                        .then().statusCode(204));

        get().then().statusCode(200).body("size()", is(0));
    }

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }



}
