package com.example.demo;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@EnableR2dbcRepositories
public class DemoApplication {

    private final static Class[] autoConfigurationClasses = {ReactiveWebServerFactoryAutoConfiguration.class,
            // web
            HttpHandlerAutoConfiguration.class,
            WebFluxAutoConfiguration.class,
            ErrorWebFluxAutoConfiguration.class,

            // data
            R2dbcAutoConfiguration.class,
            R2dbcDataAutoConfiguration.class};

    public static SpringApplication buildApp() {
        var translationService = new TranslationService();

        return new SpringApplicationBuilder(DemoApplication.class)
                .sources(autoConfigurationClasses)
                .initializers((GenericApplicationContext applicationContext) -> {
                    applicationContext.registerBean(ConnectionFactoryInitializer.class, () -> {
                        ConnectionFactory connectionFactory = applicationContext.getBean(ConnectionFactory.class);

                        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
                        initializer.setConnectionFactory(connectionFactory);
                        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("init.sql")));

                        return initializer;
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
