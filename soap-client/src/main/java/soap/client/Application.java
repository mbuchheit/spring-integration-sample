package soap.client;

/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.net.MalformedURLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.ws.DefaultSoapHeaderMapper;
import org.springframework.integration.ws.SimpleWebServiceOutboundGateway;
import org.springframework.messaging.MessageChannel;
import org.springframework.ws.client.support.destination.Wsdl11DestinationProvider;

import de.hfu.vbank.GetCreditRate;

@Configuration
@ComponentScan
@IntegrationComponentScan
@EnableAutoConfiguration
public class Application {
	
	String myPayload1 = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns2:getCreditRate xmlns:ns2=\"http://vbank.hfu.de/\"><arg0>2000.0</arg0><arg1>12</arg1><arg2>a</arg2></ns2:getCreditRate></S:Body></S:Envelope>";
	String myPayload2 = "<ns2:getCreditRate xmlns:ns2=\"http://vbank.hfu.de/\"><arg0>2000.0</arg0><arg1>12</arg1><arg2>a</arg2></ns2:getCreditRate>";
	String myPayload3 = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns2:getCreditRate xmlns:ns2=\"http://vbank.hfu.de/\"><arg0>2000.0</arg0><arg1>12</arg1><arg2>a</arg2></ns2:getCreditRate></S:Body></S:Envelope>";

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(
				Application.class, args);
		TempConverter converter = ctx.getBean(TempConverter.class);

		GetCreditRate getCreditRate = new GetCreditRate();
		getCreditRate.setArg0(1000.00);
		getCreditRate.setArg1(5);
		getCreditRate.setArg2("a");

		System.out.println(converter.fahrenheitToCelcius(getCreditRate));
		ctx.close();
	}

	@MessagingGateway
	public interface TempConverter {

		@Gateway(requestChannel = "requestChannel")
		float fahrenheitToCelcius(GetCreditRate getCreditRate);

	}

	@Bean
	public HttpRequestHandlingMessagingGateway httpGate() {
		HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(
				true);
		RequestMapping mapping = new RequestMapping();
		mapping.setMethods(HttpMethod.GET);
		mapping.setPathPatterns("/foo");
		gateway.setRequestMapping(mapping);
		gateway.setRequestChannel(requestChannel());
		gateway.setRequestPayloadType(String.class);
		return gateway;
	}

	@Bean
	public MessageChannel requestChannel() {
		return new DirectChannel();
	}

	@Bean
	public Wsdl11DestinationProvider wsdl11DestinationProvider() {
		Wsdl11DestinationProvider wsdl11DestinationProvider = new Wsdl11DestinationProvider();
		try {
			wsdl11DestinationProvider
					.setWsdl(new UrlResource(
							"http://localhost:8181/VBankService/services/VBankPort?wsdl"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		System.out.println(wsdl11DestinationProvider.getDestination());

		return wsdl11DestinationProvider;
	}

	@Bean
	public SimpleWebServiceOutboundGateway webserviceGate() {
		SimpleWebServiceOutboundGateway simpleWebServiceOutboundGateway = new SimpleWebServiceOutboundGateway(
				wsdl11DestinationProvider());
		simpleWebServiceOutboundGateway.setHeaderMapper(new DefaultSoapHeaderMapper());

		// simpleWebServiceOutboundGateway.setOutputChannel(requestChannel());
		return simpleWebServiceOutboundGateway;
	}

	@Bean
	public IntegrationFlow convert() {
		return IntegrationFlows
				.from("requestChannel")
				.transform(payload -> myPayload2)
				.handle(webserviceGate()).get();
	}
}
