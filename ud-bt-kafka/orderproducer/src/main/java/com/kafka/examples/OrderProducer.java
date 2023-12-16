package com.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class OrderProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");

        try (KafkaProducer<String, Integer> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, Integer> record = new ProducerRecord<>("orders", "MacBookPro", 10);
            //async send
            producer.send(record, (metadata, e) -> {
                System.out.println(metadata);
                if (e != null) {
                    System.out.println(e.getMessage());
                }
            });
            System.out.println("Message sent!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
