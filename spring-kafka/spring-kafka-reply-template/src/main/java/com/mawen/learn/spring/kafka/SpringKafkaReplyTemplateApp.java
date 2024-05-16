package com.mawen.learn.spring.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/15
 */
@EnableKafka
@SpringBootApplication
public class SpringKafkaReplyTemplateApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaReplyTemplateApp.class, args);
	}
}
