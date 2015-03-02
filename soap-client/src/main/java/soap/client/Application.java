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

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.integration.ws.SimpleWebServiceInboundGateway;
import org.springframework.integration.ws.SimpleWebServiceOutboundGateway;
import org.springframework.integration.ws.WebServiceHeaders;
import org.springframework.messaging.Message;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;

import de.hfu.vbank.GetCreditRate;
import de.hfu.vbank.ObjectFactory;


@Configuration
@ComponentScan
@IntegrationComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(
				Application.class, args);
	
		GetCreditRate getCreditRate = new GetCreditRate();
		getCreditRate.setArg0(1000.00);
		getCreditRate.setArg1(12);
		getCreditRate.setArg2("a");
		
		System.out.println(new ObjectFactory().createGetCreditRate(getCreditRate));
	}

	@MessagingGateway
	public interface CreditRate {

		@Gateway(requestChannel = "creditRate.input")
		double customerSolventToCreditRate(double arg0, int arg1, String arg2);
	}

	@Bean
	public IntegrationFlow creditRate() {
		/*return IntegrationFlows.from("creditRate")
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println(payload.getPayload().getClass()
								+ " " + payload.getPayload());
						return payload;
					}
				}).get();*/
		
		return f -> f.enrichHeaders(h -> h.header(WebServiceHeaders.SOAP_ACTION, "Test")).
				handle(new SimpleWebServiceInboundGateway().s)
	}
}
