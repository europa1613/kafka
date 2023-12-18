package com.kafka.examples.customer.deserializers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class OrderCSConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", OrderDeserializer.class.getName());
        props.put("group.id", "OrderGroup"); // Consumer group with scoped for each topic

        try (KafkaConsumer<String, Order> consumer = new KafkaConsumer<>(props)) {
//            consumer.subscribe(Collections.singletonList("orders-cs"));
            consumer.subscribe(Collections.singletonList("orders-partitioned"));

            ConsumerRecords<String, Order> records = consumer.poll(Duration.ofSeconds(30));
            for (ConsumerRecord<String, Order> record : records) {
                System.out.println("customer: " + record.key());
                System.out.println("Order: " + record.value());
                System.out.println("Partition: " + record.partition());
            }
        }
    }
}
