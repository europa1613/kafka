package com.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class OrderProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());

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
