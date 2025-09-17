package groffe.testes.apiconsumobackend;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EndpointRoute extends RouteBuilder {

	@Value("${wait.time}")
	private int waitTime;
	
	@Value("${consumo.backend.url}")
	private String consumoBackendUrl;

    @Value("${consumo.backend.port}")
    private int consumoBackendPort;

    @Override
    public void configure() throws Exception {

        from("jetty:http://0.0.0.0:" + consumoBackendPort + "/consumobackend")
            .routeId("json-endpoint-route")
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    RestTemplate restTemplate = new RestTemplate();
                    String responseApiContagem = null;
                    try {
                        responseApiContagem = restTemplate.getForObject(consumoBackendUrl, String.class);
                    } catch (Exception e) {
                        responseApiContagem = "Erro ao enviar requisição ao Back-End: " + e.getMessage();
                    }

                    /*System.out.println();
                	System.out.println();
                    System.out.println("Aguardando 50 milissegundos...");
	            	Span spanTempoEspera = ott.getTracer().spanBuilder("TempoEspera").startSpan();                	
	            	spanTempoEspera.setAttribute("tempo_inicio", System.currentTimeMillis());
	            	spanTempoEspera.setAttribute("urlApiContagem", urlApiContagem);
	            	spanTempoEspera.setAttribute("responseApiContagem", responseApiContagem);
	            	spanTempoEspera.setAttribute("urlApiSaudacoes", urlApiSaudacoes);
	            	spanTempoEspera.setAttribute("responseApiSaudacoes", responseApiSaudacoes);
	            	Thread.sleep(50);
	            	spanTempoEspera.setAttribute("tempo_termino", System.currentTimeMillis());
	            	spanTempoEspera.end();
                    System.out.println("Encerrada a espera de 50 milissegundos!");

                	System.out.println();
                	System.out.println();
                	*/


                    MensagemResponse resposta = new MensagemResponse(
					    UUID.randomUUID().toString(),
						"Ola mundo - Apache Camel + Spring!",
						responseApiContagem);
                    exchange.getIn().setBody(resposta);
                }
            })
            .marshal().json();
    }
}