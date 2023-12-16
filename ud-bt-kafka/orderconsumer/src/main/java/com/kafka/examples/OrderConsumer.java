package com.kafka.examples;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class OrderConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("group.id", "OrderGroup");

        try (KafkaConsumer<String, Integer> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("orders"));

            ConsumerRecords<String, Integer> records = consumer.poll(Duration.ofSeconds(30));
            for (ConsumerRecord<String, Integer> record : records) {
                System.out.println("Product Name: " + record.key());
                System.out.println("Quantity: " + record.value());
            }
        }
    }
}
