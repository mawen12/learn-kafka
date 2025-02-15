package com.mawen.learn.kafka.sdk.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProducerDemo {

    public static final Logger log = LoggerFactory.getLogger(ProducerDemo.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("linger.ms", "1");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        Future<RecordMetadata> send = producer.send(new ProducerRecord<>("quick-topic", "1", "1"));

        RecordMetadata recordMetadata = send.get();

        log.info("Topic:{}, partition:{}, offset: {}, timestamp:{}",
                recordMetadata.topic(),
                recordMetadata.partition(),
                recordMetadata.offset(),
                recordMetadata.timestamp());

        producer.close();
    }
}
