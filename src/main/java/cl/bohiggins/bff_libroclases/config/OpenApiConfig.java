package cl.bohiggins.bff_libroclases.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI bffLibroclasesOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("BFF Libro de Clases - Colegio Bernardo OHiggins")
						.description("Backend For Frontend que maneja llamadas a ms-academico y ms-asistencia con Circuit Breaker.")
						.version("1.0.0"));
	}
}
