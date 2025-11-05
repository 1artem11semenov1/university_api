package _inc.studentApp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI oAPI(){
        return new OpenAPI()
                .servers(
                        List.of(new Server().url("http://localhost:8080"))
                )
                .info(
                        new Info()
                                .title("University api")
                                .description("University API - веб-приложение для хранения данных об университете. Целью создания данного api является доступ к расписанию и связанным с ним данными а так же их добавление. ")
                );
    }

}
