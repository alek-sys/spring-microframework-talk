package com.example.demo;

import io.r2dbc.spi.ConnectionFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
public class DemoApplication {

    public static SpringApplication buildApp() {
        var translationService = new TranslationService();

        return new SpringApplicationBuilder(DemoApplication.class)
                .initializers((GenericApplicationContext applicationContext) -> {

                    applicationContext.registerBean(CommandLineRunner.class, () -> appArgs -> {
                        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(
                                new ClassPathResource("schema.sql"),
                                new ClassPathResource("data.sql")
                        );

                        databasePopulator.populate(applicationContext.getBean(ConnectionFactory.class)).block();
                    });

                    applicationContext.registerBean(RouterFunction.class, () -> {
                        var repo = applicationContext.getBean(BookRepository.class);
                        return route()
                                .GET("/book", request -> {
                                    var lang = request.queryParam("lang").orElse("");
                                    var translatedBooks = repo
                                            .findAll()
                                            .map(book -> new Book(
                                                    book.getId(),
                                                    translationService.translateTitle(lang, book.getTitle())
                                            ));

                                    return ServerResponse.ok().body(translatedBooks, Book.class);
                                })
                                .build();
                    });
                })
                .build();
    }

    public static void main(String[] args) {
        buildApp().run(args);
    }

}
