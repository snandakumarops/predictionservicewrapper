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



				rest("/customer-context")
						.get()
						.route()
						.setProperty("custId",simple("${header.custId}"))
						.setProperty("eventType",simple("${header.eventType}"))
						.setHeader(Exchange.HTTP_METHOD, constant("GET"))
						.removeHeader("*")
						.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
						.to("http4://portfolio-customer-event-context.apps.cluster-flrda-91e7.flrda-91e7.example.opentlc.com/odata/portfolio/customerOfferContext?bridgeEndpoint=true")
						.bean(TransformerBean.class,"lookUpCustId")
						.removeHeader("*")
						.log("${body}")
						.setHeader(Exchange.HTTP_METHOD, constant("POST"))
						.to("http://ceh-ceph-seldon-model-odh-context.apps.cluster-flrda-91e7.flrda-91e7.example.opentlc.com/predict?bridgeEndpoint=true")
						.bean(TransformerBean.class,"returnSegment")
						;
		}
		};


	}


}
