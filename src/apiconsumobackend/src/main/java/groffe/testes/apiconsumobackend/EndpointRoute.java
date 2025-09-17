package groffe.testes.apiconsumobackend;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.opentelemetry.OpenTelemetryTracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.opentelemetry.api.trace.Span;

@Component
public class EndpointRoute extends RouteBuilder {

    @Value("${consumo.backend.url}")
	private String consumoBackendUrl;

    @Value("${consumo.backend.port}")
    private int consumoBackendPort;

    @Override
    public void configure() throws Exception {

        OpenTelemetryTracer ott = new OpenTelemetryTracer();
        ott.init(this.getContext());

        from("jetty:http://0.0.0.0:" + consumoBackendPort + "/consumobackend")
            .routeId("json-endpoint-route")
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    System.out.println();
                	System.out.println();
                    System.out.println("***** Enviando requisicao... *****");
                    RestTemplate restTemplate = new RestTemplate();
                    String responseBackEnd = null;
                    try {
                        responseBackEnd = restTemplate.getForObject(consumoBackendUrl, String.class);
                    } catch (Exception e) {
                        responseBackEnd = "Erro ao enviar requisição ao Back-End: " + e.getMessage();
                    }
                    System.out.println("# Resposta do Back-End:");
                    System.out.println(responseBackEnd);

                    System.out.println();
                	System.out.println();
                    System.out.println("Aguardando 50 milissegundos...");
	            	Span spanTempoEspera = ott.getTracer().spanBuilder("TempoEspera").startSpan();                	
	            	spanTempoEspera.setAttribute("tempo_inicio", System.currentTimeMillis());
	            	spanTempoEspera.setAttribute("consumoBackendUrl", consumoBackendUrl);
	            	spanTempoEspera.setAttribute("responseBackEnd", responseBackEnd);
	            	Thread.sleep(50);
	            	spanTempoEspera.setAttribute("tempo_termino", System.currentTimeMillis());
	            	spanTempoEspera.end();
                    System.out.println("Encerrada a espera de 50 milissegundos!");

                	System.out.println();
                	System.out.println();

                    MensagemResponse resposta = new MensagemResponse(
					    UUID.randomUUID().toString(),
						"Ola mundo - Apache Camel + Spring!",
						responseBackEnd);
                    exchange.getIn().setBody(resposta);
                }
            })
            .marshal().json();
    }
}