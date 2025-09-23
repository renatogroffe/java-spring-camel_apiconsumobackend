package groffe.testes.apiconsumobackend;

import org.apache.camel.opentelemetry.starter.CamelOpenTelemetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@CamelOpenTelemetry
public class ApiconsumobackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiconsumobackendApplication.class, args);
	}

}
