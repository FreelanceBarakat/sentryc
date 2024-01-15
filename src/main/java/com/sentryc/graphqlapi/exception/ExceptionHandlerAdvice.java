package com.sentryc.graphqlapi.exception;


import graphql.kickstart.spring.error.ThrowableGraphQLError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ThrowableGraphQLError handleRuntimeException(
            final Throwable exception
    ) {
        log.error(exception.getMessage(), exception);

        return new ThrowableGraphQLError(new RuntimeException("INTERNAL_SERVER_ERROR"));
    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ThrowableGraphQLError handleGeneralException(
            final Throwable exception
    ) {
        log.error(exception.getMessage(), exception);
        return new ThrowableGraphQLError(new RuntimeException("INTERNAL_SERVER_ERROR"));

    }

}