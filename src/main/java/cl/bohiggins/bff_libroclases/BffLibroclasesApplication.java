package cl.bohiggins.bff_libroclases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BffLibroclasesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BffLibroclasesApplication.class, args);
	}

}
