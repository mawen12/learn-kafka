package com.mawen.learn.spring.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/15
 */
@Configuration
public class KafkaConfig {

	@Bean
	public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
			ProducerFactory<String, String> producerFactory,
			ConcurrentKafkaListenerContainerFactory<String, String> factory,
			KafkaTemplate<String, String> template) {

		factory.setReplyTemplate(template);
		ConcurrentMessageListenerContainer<String, String> container = factory.createContainer("replies");
		container.getContainerProperties().setGroupId("replies");

		ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory, container);
		replyingKafkaTemplate.setDefaultTopic("requests");
		return replyingKafkaTemplate;
	}

	@Bean
	public KafkaTemplate<String, String> template(ProducerFactory<String, String> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}

	@Bean
	public NewTopic topic1() {
		return TopicBuilder.name("requests").partitions(1).replicas(1).build();
	}

	@KafkaListener(id = "upperCaser", topics = "requests")
	@SendTo
	public String upperCaseIt(String in) {
		System.out.println("Request: " + in);
		return in.toUpperCase();
	}
}
