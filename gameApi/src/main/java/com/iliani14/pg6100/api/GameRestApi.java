package com.iliani14.pg6100.api;

/**
 * Created by anitailieva on 15/11/2016.
 */

import com.iliani14.pg6100.dto.AnswerDto;
import com.iliani14.pg6100.dto.GameDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/games", description = "Handling and retrieving quizzes" )
@Path("/games")
@Produces({
        Formats.V1_JSON
})

public interface GameRestApi {



    @ApiOperation("Retrieve all active qames")
    @GET
    List<GameDto> getAllActiveGames();



    @ApiOperation("Create a new game")
    @POST
    @Consumes(Formats.V1_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created game")
    Response createNewGame(
            @ApiParam("Number of quizzes in the game. 5 by default")
            @QueryParam("numberOfQuestions")
                    String numberOfQuestions);


    @ApiOperation("Get a game by id")
    @GET
    @Path("/{id}")
    GameDto getGameById(
            @ApiParam("The id of the game")
            @PathParam("id")
                    Long id);



    @ApiOperation("Answer the question")
    @POST
    @Path("/{id}")
    AnswerDto answerTheQuestion(
            @ApiParam("The id of the game")
            @PathParam("id")
                    Long id,
            @ApiParam("The answer to the question")
            @QueryParam("correctAnswer")
                    String correctAnswer);



    @ApiOperation("Stop the game")
    @DELETE
    @Path("/{id}")
    void endGame(
            @ApiParam("The id of the game")
            @PathParam("id")
                    Long id);

}