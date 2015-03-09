package rest.json;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
@ComponentScan
@IntegrationComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@MessagingGateway(defaultRequestChannel = "requestChannel")
	public interface Gateway {
		String sendReceive(String in);
	}

	@Bean
	public HttpRequestHandlingMessagingGateway httpGate() {
		HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(
				true);
		RequestMapping mapping = new RequestMapping();
		mapping.setMethods(HttpMethod.POST);
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
	public IntegrationFlow flow() {
		return IntegrationFlows
				.from("requestChannel")
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println(payload.getPayload().getClass()
								+ " " + payload.getPayload());
						return payload;
					}

				})
				.channel("TestChannel1")
				.transform(new JsonToObjectTransformer(User.class))
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println("XXXXXXXXX"
								+ payload.getPayload() + " "
								+ payload.getPayload().getClass());
						return payload;
					}
				}).get();
	}
	
	@Bean
	public IntegrationFlow testXChannel1() {
		return IntegrationFlows.from("TestChannel1")
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println("TestChannel1: "
								+ payload.getPayload() + " "
								+ payload.getPayload().getClass());
						return payload;
					}
				})
				.channel("TestChannel2")
				.get();
	}
	
	@Bean
	public IntegrationFlow testXChannel2() {
		return IntegrationFlows.from("TestChannel2")
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println("TestChannel2: "
								+ payload.getPayload() + " "
								+ payload.getPayload().getClass());
						return payload;
					}
				})
				.channel("testXChannel3.input")
				.get();
	}
	
	@Bean
	public IntegrationFlow testXChannel3() {
		return f -> f
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println("testXChannel3: "
								+ payload.getPayload() + " "
								+ payload.getPayload().getClass());
						return payload;
					}
				});
	}

	
}
