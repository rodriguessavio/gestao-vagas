package br.com.saviorodrigues.gestao_vagas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
//Bean para sobrescrever uma implementação que já existe
    @Bean
    public OpenAPI openAPI() {
        return  new OpenAPI()
                .info(new Info().title("Gestão de Vagas").description("API responsável pela gestão de vagas").version("1"))
                .schemaRequirement("jwt_auth", creaSecurityScheme());
    }

    private SecurityScheme creaSecurityScheme(){
        return new SecurityScheme().name("jwt_auth").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
    }


}
