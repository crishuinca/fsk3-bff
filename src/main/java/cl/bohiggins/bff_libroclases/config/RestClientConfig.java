package cl.bohiggins.bff_libroclases.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	@Value("${ms-academico.url}")
	private String urlAcademico;

	@Value("${ms-asistencia.url}")
	private String urlAsistencia;

	@Bean
	public RestClient academicoRestClient() {
		return RestClient.builder()
				.baseUrl(urlAcademico)
				.build();
	}

	@Bean
	public RestClient asistenciaRestClient() {
		return RestClient.builder()
				.baseUrl(urlAsistencia)
				.build();
	}
}
