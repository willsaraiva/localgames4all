package ufc.pds.locagames4all.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Links;


@Configuration

public class SwaggerConfig {
    @Bean
    public OpenAPI LocaGames4all() {
        SpringDocUtils.getConfig().addResponseTypeToIgnore(Links.class);
        return new OpenAPI()
                .info(new Info().title("LocaGames4all API")
                        .description("Sua API de locação de jogos de mesa!")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
