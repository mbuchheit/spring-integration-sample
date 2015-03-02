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
import org.springframework.integration.annotation.BridgeTo;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.GenericHandler;
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
		System.out.println(ctx.getBean(FooService.class).foo("foo"));

	}

	@MessagingGateway(defaultRequestChannel = "flow.input")
	public static interface FooService {

		String foo(String request);
	}

	@Bean
	IntegrationFlow foo() {
		return f -> f.routeToRecipients(r -> r.recipient("bridge1", "true")
				.recipient("bridge2", "true"));
	}

	@BridgeTo
	@Bean
	public MessageChannel bridge1() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel bridge2Channel() {
		return new DirectChannel();
	}
	
	@Bean
	IntegrationFlow flow(){
		return f -> f
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println("f "+payload.getPayload().getClass()
								+ " " + payload.getPayload());
						return payload;
					}
				});				
	}

	@Bean
	IntegrationFlow bridge2(){
		return IntegrationFlows.from(bridge2Channel())
				.handle(new GenericHandler<Message>() {
					@Override
					public Object handle(Message payload,
							Map<String, Object> headers) {
						System.out.println(payload.getPayload().getClass()
								+ " " + payload.getPayload());
						return payload;
					}
				}).get();				
	}
}
