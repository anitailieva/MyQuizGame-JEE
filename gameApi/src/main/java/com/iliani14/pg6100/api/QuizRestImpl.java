package com.iliani14.pg6100.api;

import com.google.common.base.Throwables;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;

/**
 * Created by anitailieva on 15/11/2016.
 */
public class QuizRestImpl{




    private WebApplicationException wrapException(Exception e) throws WebApplicationException {
        Throwable cause = Throwables.getRootCause(e);
        if (cause instanceof ConstraintViolationException) {
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }

}
