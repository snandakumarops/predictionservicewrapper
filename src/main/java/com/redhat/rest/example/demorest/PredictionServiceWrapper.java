package com.redhat.rest.example.demorest;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PredictionServiceWrapper {

	@Value("${prediction.service.url}")
	private String predictionUrl;

	@Value("${odata.service.url}")
	private String dataServiceUrl;


	public static void main(String[] args) {

		SpringApplication.run(PredictionServiceWrapper.class, args);
	}




	@Bean
	public RouteBuilder routeBuilder() {
		return new RouteBuilder() {





			@Override
			public void configure() throws Exception {
				System.out.println("prediction service"+predictionUrl);
				System.out.println("odata url"+dataServiceUrl);



				restConfiguration()
						.component("servlet")
						.bindingMode(RestBindingMode.auto)
						.producerComponent("http4").host("localhost:8080");

				String predictionUrl = System.getProperty("prediction.service.url");
				String odataUrl = System.getProperty("odata.service.url");


				rest("/customer-context")
						.get()
						.route()
						.setProperty("custId",simple("${header.custId}"))
						.setProperty("eventType",simple("${header.eventType}"))
						.setHeader(Exchange.HTTP_METHOD, constant("GET"))
						.removeHeader("*")
						.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
						.to(dataServiceUrl+"?bridgeEndpoint=true")
						.bean(TransformerBean.class,"lookUpCustId")
						.removeHeader("*")
						.log("${body}")
						.setHeader(Exchange.HTTP_METHOD, constant("POST"))
						.to(predictionUrl+"?bridgeEndpoint=true")
						.bean(TransformerBean.class,"returnSegment")
						;
		}
		};


	}


}
