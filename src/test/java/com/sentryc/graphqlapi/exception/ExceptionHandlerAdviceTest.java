package com.sentryc.graphqlapi.exception;


import graphql.kickstart.spring.error.ThrowableGraphQLError;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExceptionHandlerAdviceTest {
    private static final String MESSAGE = "message";

    @InjectMocks
    private ExceptionHandlerAdvice exceptionHandlerAdvice;

    @Test
    void handleBadRequest_when_receiving_exception_should_handle_successfully() {
        assertThat(exceptionHandlerAdvice.handleRuntimeException(new IllegalArgumentException(MESSAGE)))
                .isNotNull()
                .isInstanceOf(ThrowableGraphQLError.class);
    }


    @Test
    void handleGeneralException_when_receiving__exception_should_handle_successfully() {
        assertThat(exceptionHandlerAdvice.handleGeneralException(new Throwable(MESSAGE)))
                .isNotNull()
                .isInstanceOf(ThrowableGraphQLError.class);
    }
}
