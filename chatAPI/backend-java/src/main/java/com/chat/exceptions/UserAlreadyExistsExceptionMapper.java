package com.chat.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asazawal on 10/21/17.
 */
@Provider
public class UserAlreadyExistsExceptionMapper extends AbstractException implements ExceptionMapper<UserAlreadyExistsException> {
    @Override
    public Response toResponse(UserAlreadyExistsException exception) {
        try {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(getDefaultMessage(exception.getCode(),exception.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception.getMessage())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }






}
