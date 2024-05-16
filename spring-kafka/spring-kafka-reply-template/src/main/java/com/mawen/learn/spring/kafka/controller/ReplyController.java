package com.mawen.learn.spring.kafka.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;

import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/15
 */
@RestController
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyingKafkaTemplate<String, String, String> template;

	@GetMapping("/get/foo/{data}")
	public String get(@PathVariable("data") String data) {
		RequestReplyMessageFuture<String, String> future = this.template.sendAndReceive(MessageBuilder.withPayload(data).build());

		AtomicReference<String> content = new AtomicReference<>();
		future.whenComplete((msg, ex) -> content.set((String) msg.getPayload()));


		if (content.get() != null) {
			return content.get() + "\n";
		}
		else {
			return "no result yet\n";
		}
	}

	@GetMapping("/get/foo1/{data}")
	public String get1(@PathVariable("data") String data) throws ExecutionException, InterruptedException {
		// create producer record
		ProducerRecord<String, String> record = new ProducerRecord<>("requests", data);
		// set reply topic in header
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "replies".getBytes()));
		// post in kafka topic
		RequestReplyFuture<String, String, String> sendAndReceive = template.sendAndReceive(record);
		// confirm if producer produced successfully
//		SendResult<String, String> sendResult = sendAndReceive.getSendFuture().get();

		// print all headers
//		sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

		// get consumer record
		ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();
		// return consumer value
		return consumerRecord.value();
	}

}
