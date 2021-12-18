package com.rabbit.consumer.consumertest.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class RabbitConfiguration {

	@Bean
	public Queue queuePedido() {
		return QueueBuilder				
				.durable("pedido-spring")				
				.deadLetterExchange("pedido.deadletter")
				.deadLetterRoutingKey("pedido.deadletter")				
				.deliveryLimit(5)				
				.build();	
	}
	
	@Bean
	public Exchange exchangePedido() {
			return ExchangeBuilder
					.topicExchange("order.publish")										
					.build();					
	}
	
	
	@Bean
	public Binding bindingPedido() {
		return BindingBuilder
				.bind(this.queuePedido())
				.to(this.exchangePedido())
				.with("order.created.*")
				.noargs();
					
		
	}
	
	@Bean
	public Queue queuePedidoDeadLetter() {
		return QueueBuilder				
				.durable("pedido-deadletter-spring")
				.autoDelete()	
				.build();	
	}
	
	@Bean
	public Exchange exchangePedidoDeadLetter() {
			return ExchangeBuilder
					.topicExchange("pedido.deadletter")										
					.build();
	}

	@Bean
	public Binding bindingPedidoDeadLetter() {
		return BindingBuilder
				.bind(this.queuePedidoDeadLetter())
				.to(this.exchangePedidoDeadLetter())
				.with("pedido.deadletter")
				.noargs();
					
		
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(this.jsonMessageConverter());
		return rabbitTemplate;
		
	}
	
		
	

}
