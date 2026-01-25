package com.bloodlink.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RequestPredicates.pathExtension;
import static org.springframework.web.servlet.function.RouterFunctions.route;


@Configuration
@EnableRetry
public class WebConfig implements WebMvcConfigurer {

    /**
     * SPA router for local development only.
     * In Docker, nginx serves static files directly.
     */
    @Bean
    @Profile("!docker")
    RouterFunction<ServerResponse> spaRouter() {
        ClassPathResource index = new ClassPathResource("static/index.html");
        List<String> extensions = Arrays.asList("js", "css", "ico", "png", "jpg", "gif", "svg", "html");
        RequestPredicate spaPredicate = path("/api/**")
                .or(path("/error"))
                .or(path("/swagger-ui/**"))
                .or(path("/swagger-ui.html"))
                .or(path("/v3/**"))
                .or(path("/api-docs"))
                .or(path("/api-docs/**"))
                .or(path("/actuator/**"))
                .or(pathExtension(extensions::contains))
                .negate();
        return route().resource(spaPredicate, index).build();
    }
}