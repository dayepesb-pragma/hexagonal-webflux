package co.pragma.scaffold.infra.driver.reactive.adapater;

import co.pragma.scaffold.infra.driver.reactive.handler.AuthHandler;
import co.pragma.scaffold.infra.driver.reactive.handler.Handler;
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
    public RouterFunction<ServerResponse> routerFunction(Handler handler, AuthHandler authHandler) {
        return route(GET("/api/user")
                .and(RequestPredicates.accept(MediaType.TEXT_EVENT_STREAM)), handler::findAll)
                .andRoute(POST("/api/user/register"), handler::registerUser)
                .andRoute(POST("/api/auth/login"), authHandler::login)
                ;
                //.andRoute(GET("/api/user/{id}"), handler::findById)
                //.andRoute(POST("/api/user"), handler::create);
    }
}
