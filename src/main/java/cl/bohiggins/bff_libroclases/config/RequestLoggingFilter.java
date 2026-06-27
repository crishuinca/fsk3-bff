package cl.bohiggins.bff_libroclases.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		long inicio = System.currentTimeMillis();
		String metodo = request.getMethod();
		String uri = request.getRequestURI();
		log.info("Request {} {}", metodo, uri);
		try {
			filterChain.doFilter(request, response);
		} finally {
			log.info("Response {} {} -> {} ({} ms)", metodo, uri, response.getStatus(), System.currentTimeMillis() - inicio);
		}
	}
}
