package com.sentryc.graphqlapi.controller;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.junit5.api.DBRider;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@DBRider
@DBUnit(alwaysCleanBefore = true,
        schema = "public",
        url = "jdbc:h2:mem:testdb",
        driver = "org.h2.Driver",
        user = "sa",
        password = "",
        leakHunter = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SellerControllerIntegrationTest {

    @Value("classpath:queries/requests/request1.graphql")
    private Resource request;

    @Value("classpath:queries/responses/expected_response.json")
    private Resource expectedResponse;
    private static final String GRAPHQL_PATH = "/graphql";


    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DataSet(
            value = "datasets/controller_integration_test_initial_data.yaml",
            strategy = SeedStrategy.INSERT,
            disableConstraints = true,
            cleanAfter = true,
            transactional = true
    )
    void get_Sellers_when_given_filters_should_return_results() throws Exception {
        webTestClient.post()
                .uri(GRAPHQL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(toJSON(toString(request))), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(toString(expectedResponse));
    }

    private String toString(final Resource file) throws IOException {
        return new String(file.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static String toJSON(String query) {
        try {
            return new JSONObject().put("query", query).toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}