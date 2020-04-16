package com.redhat.rest.example.demorest;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PredictionServiceWrapper {

	public static void main(String[] args) {

		SpringApplication.run(PredictionServiceWrapper.class, args);
	}




	@Bean
	public RouteBuilder routeBuilder() {
		return new RouteBuilder() {



			@Override
			public void configure() throws Exception {


				restConfiguration()
						.component("servlet")
						.bindingMode(RestBindingMode.auto)
						.producerComponent("http4").host("localhost:8080");


				//start case from the online banking website
				rest("/predict")
						.get()
						.enableCORS(true)
						.route()
						.bean(TransformerBean.class,"transformResponse(${header.income},${header.eventType},${header.lastOfferResponse})")
						.removeHeader("*")
						.setHeader(Exchange.HTTP_METHOD, constant("POST"))
						.to("http4://ceh-seldon-models-customer-event-context.apps.cluster-flrda-91e7.flrda-91e7.example.opentlc.com/predict?bridgeEndpoint=true")
						.bean(TransformerBean.class,"returnSegment");

// .log("${body}")
//                        .endRest();

		}
		};


	}


}
