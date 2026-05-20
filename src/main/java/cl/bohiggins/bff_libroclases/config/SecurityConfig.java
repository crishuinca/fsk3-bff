package cl.bohiggins.bff_libroclases.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import cl.bohiggins.bff_libroclases.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
						.requestMatchers(
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/v3/api-docs/**",
								"/actuator/**",
								"/h2-console/**")
						.permitAll()
						.requestMatchers(HttpMethod.GET, "/api/v1/auth/usuarios").hasRole("INSPECTOR")
						.requestMatchers(HttpMethod.GET, "/api/v1/auth/proximo-estudiante-id").hasRole("INSPECTOR")
						.requestMatchers(HttpMethod.POST, "/api/v1/auth/crearUsuario").hasRole("INSPECTOR")
						.requestMatchers(HttpMethod.PUT, "/api/v1/auth/usuarios/*/vinculo-estudiante/*").hasRole("INSPECTOR")
						.requestMatchers(HttpMethod.POST, "/api/v1/anotaciones").hasRole("PROFESOR")
						.requestMatchers(HttpMethod.POST, "/api/v1/asistencias").hasAnyRole("PROFESOR", "INSPECTOR")
						.anyRequest().authenticated())
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint((request, response, authException) -> {
							response.setStatus(401);
							response.setContentType("application/json");
							response.getWriter().write("{\"error\":\"Debe iniciar sesion. Use POST /api/v1/auth/login y Authorize en Swagger.\"}");
						})
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.setStatus(403);
							response.setContentType("application/json");
							response.getWriter().write("{\"error\":\"No tiene permisos. Este endpoint requiere rol INSPECTOR.\"}");
						}))
				.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
