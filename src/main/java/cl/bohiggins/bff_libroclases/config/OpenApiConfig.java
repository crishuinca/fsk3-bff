package cl.bohiggins.bff_libroclases.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	private static final String BEARER_AUTH = "bearerAuth";

	@Bean
	public OpenAPI bffLibroclasesOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("BFF Libro de Clases - Colegio Bernardo OHiggins")
						.description("Backend For Frontend que maneja llamadas a ms-academico y ms-asistencia con Circuit Breaker.")
						.version("1.0.0"))
				.addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
				.components(new Components().addSecuritySchemes(BEARER_AUTH,
						new SecurityScheme()
								.name(BEARER_AUTH)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
								.description("Obtenga el token con POST /api/v1/auth/login e ingreselo aqui (solo el token, sin la palabra Bearer).")));
	}
}
