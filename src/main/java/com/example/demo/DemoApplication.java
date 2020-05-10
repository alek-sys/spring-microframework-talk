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

                    applicationContext.registerBean(BookController.class,
                            () -> new BookController(applicationContext.getBean(BookRepository.class), translationService));
                    // or applicationContext.registerBean(BookController.class) to delegate it back to Spring

                    applicationContext.registerBean(TranslationService.class,
                            () -> translationService);
                })
                .build();
    }

    public static void main(String[] args) {
        buildApp().run(args);
    }

}
