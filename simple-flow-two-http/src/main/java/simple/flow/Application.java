package simple.flow;

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
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
@ComponentScan
@IntegrationComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(
				Application.class, args);
	}

	@MessagingGateway(defaultRequestChannel = "requestChannel1")
	public interface Gateway {
		String sendReceive(String in);
	}

	@MessagingGateway(defaultRequestChannel = "requestChannel2")
	public interface Gate {
		String sendReceive(String in);
	}

	@Bean
	public HttpRequestHandlingMessagingGateway httpGate1() {
		HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(
				true);
		RequestMapping mapping = new RequestMapping();
		mapping.setMethods(HttpMethod.POST);
		mapping.setPathPatterns("/requestChannel1");
		gateway.setRequestMapping(mapping);
		gateway.setRequestChannel(requestChannel1());
		gateway.setRequestPayloadType(String.class);
		return gateway;
	}
	
	
	@Bean
	public HttpRequestHandlingMessagingGateway httpGate2() {
		HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(
				true);
		RequestMapping mapping = new RequestMapping();
		mapping.setMethods(HttpMethod.POST);
		mapping.setPathPatterns("/requestChannel2");
		gateway.setRequestMapping(mapping);
		gateway.setRequestChannel(requestChannel2());
		gateway.setRequestPayloadType(String.class);
		return gateway;
	}

	@Bean
	public MessageChannel requestChannel1() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel requestChannel2() {
		return new DirectChannel();
	}

	@Bean
	public IntegrationFlow flow() {
		return IntegrationFlows
				.from("requestChannel1")
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println("requestChannel1 "+payload.getPayload().getClass()
								+ " " + payload.getPayload());
						return payload;
					}
				}).get();
	}

	@Bean
	public IntegrationFlow errorFlow() {
		return IntegrationFlows.from("requestChannel2")
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println("requestChannel2 "+payload.getPayload().getClass()
								+ " " + payload.getPayload());
						return payload;
					}
				}).get();
	}

	

}
