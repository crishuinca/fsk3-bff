package cl.bohiggins.bff_libroclases.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	@Value("${ms-academico.service-id:ms-academico}")
	private String academicoServiceId;

	@Value("${ms-asistencia.service-id:ms-asistencia}")
	private String asistenciaServiceId;

	@Value("${ms-academico.api-path:/api/v1}")
	private String academicoApiPath;

	@Value("${ms-asistencia.api-path:/api/v1}")
	private String asistenciaApiPath;

	@Bean
	@LoadBalanced
	RestClient.Builder loadBalancedRestClientBuilder() {
		return RestClient.builder();
	}

	@Bean
	public RestClient academicoRestClient(
			@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder) {
		return builder
				.baseUrl("http://" + academicoServiceId + academicoApiPath)
				.build();
	}

	@Bean
	public RestClient asistenciaRestClient(
			@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder) {
		return builder
				.baseUrl("http://" + asistenciaServiceId + asistenciaApiPath)
				.build();
	}
}
