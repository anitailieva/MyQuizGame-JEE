package com.iliani14.pg6100.api;

import io.swagger.annotations.*;

import javax.ws.rs.*;

/**
 * Created by anitailieva on 15/11/2016.
 */

@Api(value = "/category", description = "Handling and retrieving categories" )
@Path("/category")
@Produces({
        Formats.V2_JSON,
        Formats.BASE_JSON
})
public interface QuizRestApi {


}