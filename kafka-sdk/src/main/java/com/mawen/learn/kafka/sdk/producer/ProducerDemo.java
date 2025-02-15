package com.mawen.learn.kafka.sdk.producer;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

public class ProducerDemo {

    public static void main(String[] args) {
        Properties properties = new Properties();
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
    }
}
