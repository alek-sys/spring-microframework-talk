package com.example.demo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureDataR2dbc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    DatabaseClient databaseClient;

    @Test
    void shouldReturnListOfBooks() {
        webTestClient.get().uri("/book")
                .exchange()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].title").value(equalTo("A year in Provence"));
    }

    @Test
    void shouldTranslateTitle() {
        webTestClient.get().uri("/book?lang=fr")
                .exchange()
                .expectBody()
                .jsonPath("$[0].title").value(equalTo("Une année en Provence"));
    }

}
