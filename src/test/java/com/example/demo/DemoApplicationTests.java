package com.example.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.example.demo.DemoApplication.buildApp;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoApplicationTests {

    private ConfigurableApplicationContext applicationContext;
    private WebTestClient webTestClient;

    @BeforeAll
    void setUp() {
        applicationContext = buildApp().run();
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @AfterAll
    void tearDown() {
        applicationContext.close();
    }

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
