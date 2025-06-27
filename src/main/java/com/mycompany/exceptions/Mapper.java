package com.mycompany.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;

@Provider
public class Mapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        int status = 500;
        String error = "Internal Server Error";

        if (ex instanceof BookNotFoundException ||
            ex instanceof AuthorNotFoundException ||
            ex instanceof CustomerNotFoundException) {
            status = 404;
            error = "Not Found";
        } else if (ex instanceof InvalidInputException) {
            status = 400;
            error = "Invalid Input";
        } else if (ex instanceof OutOfStockException) {
            status = 409;
            error = "Out of Stock";
        } else if (ex instanceof CartNotFoundException) {
            status = 404;
            error = "Cart Not Found";
        }

        Map<String, String> response = new HashMap<>();
        response.put("error", error);
        response.put("message", ex.getMessage());

        return Response.status(status).entity(response).type(MediaType.APPLICATION_JSON).build();
    }
}