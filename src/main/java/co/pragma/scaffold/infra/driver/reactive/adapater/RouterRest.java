package co.pragma.scaffold.infra.driver.reactive.adapater;

import co.pragma.scaffold.infra.driver.reactive.handler.AuthHandler;
import co.pragma.scaffold.infra.driver.reactive.handler.Handler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/user",
            produces = { MediaType.TEXT_EVENT_STREAM_VALUE },
            method = org.springframework.web.bind.annotation.RequestMethod.GET,
            beanClass = Handler.class,
            beanMethod = "findAll"
        ),
        @RouterOperation(
            path = "/api/user/register",
            method = org.springframework.web.bind.annotation.RequestMethod.POST,
            beanClass = Handler.class,
            beanMethod = "registerUser"
        ),
        @RouterOperation(
            path = "/api/auth/login",
            method = org.springframework.web.bind.annotation.RequestMethod.POST,
            beanClass = AuthHandler.class,
            beanMethod = "login"
        )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler, AuthHandler authHandler) {
        return route(GET("/api/user")
                .and(RequestPredicates.accept(MediaType.TEXT_EVENT_STREAM)), handler::findAll)
                .andRoute(POST("/api/user/register"), handler::registerUser)
                .andRoute(POST("/api/auth/login"), authHandler::login);
    }
}
