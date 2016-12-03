package com.iliani14.pg6100.api;

/**
 * Created by anitailieva on 15/11/2016.
 */

import com.iliani14.pg6100.dto.AnswerDto;
import com.iliani14.pg6100.dto.GameDto;
import com.iliani14.pg6100.dto.collection.ListDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "/games", description = "Handling and retrieving quizzes" )
@Path("/games")
@Produces(Formats.V1_JSON)
public interface GameRestApi {


        @ApiOperation("Get all active games")
        @GET
        ListDTO<GameDto> getAllActiveGames(@ApiParam("Offset in the list of news")
                                           @QueryParam("offset")
                                           @DefaultValue("0")
                                                   Integer offset,
                                           @ApiParam("Limit of news in a single retrieved page")
                                           @QueryParam("limit")
                                           @DefaultValue("10")
                                                   Integer limit);

        @ApiOperation("Create a new game")
        @POST
        @ApiResponse(code = 200, message = "The id of newly created game")
        Response createGame(
                @ApiParam("Optional parameter specifying number of quizzes in the game. Default value is 5 if absent")
                @QueryParam("limit")
                @DefaultValue("5")
                        String limit);

        @ApiOperation("Get a game specified by id")
        @GET
        @Path("/{id}")
        GameDto getGameById(
                @ApiParam("Unique id of the game")
                @PathParam("id")
                        Long id);

        @ApiOperation("Answer the current quiz")
        @POST
        @Path("/{id}")
        AnswerDto answerQuestion(
                @ApiParam("Unique id of the game")
                @PathParam("id")
                        Long id,
                @ApiParam("Answer")
                @QueryParam("answer")
                        String answer);

        @ApiOperation("Quit the game")
        @DELETE
        @Path("/{id}")
        void endGame(
                @ApiParam("Unique id of the game")
                @PathParam("id")
                        Long id);
    }
