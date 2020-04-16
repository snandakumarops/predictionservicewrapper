package com.redhat.rest.example.demorest;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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

//
				restConfiguration()
						.component("servlet")
						.bindingMode(RestBindingMode.auto)
						.producerComponent("http4").host("localhost:8080");
//
//
//				final SSLContext sslContext = SSLContexts.custom()
//						.loadTrustMaterial(null,
//								TrustAllStrategy.INSTANCE)
//						.build();
//
//				KeyStoreParameters ksp = new KeyStoreParameters();
//				ksp.setResource("\\Projects\\example\\exampleCa.jks");
//				ksp.setPassword("password");
//				TrustManagersParameters tmp = new TrustManagersParameters();
//				tmp.setKeyStore(ksp);
//				SSLContextParameters scp = new SSLContextParameters();
//				scp.setTrustManagers(tmp);
//
//
//				rest("/customer-context")
//						.get()
//						.route()
//						.setHeader(Exchange.HTTP_METHOD, constant("GET"))
//						.to("https4://portfolio-customer-event-context.apps.cluster-flrda-91e7.flrda-91e7.example.opentlc.com/odata/portfolio/customerOfferContext?bridgeEndpoint=true&sslContextParameters="+scp+"&amp;throwExceptionOnFailure=false")
//						.log("${body}");



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



		}
		};


	}


}
