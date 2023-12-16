package com.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class OrderProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");

        try (KafkaProducer<String, Integer> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, Integer> record = new ProducerRecord<>("orders", "MacBookPro", 10);
            Future<RecordMetadata> recordMetadataFuture = producer.send(record); // Synchronous send
            RecordMetadata metadata = recordMetadataFuture.get();
            System.out.println(metadata);
            System.out.println("Message sent!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
