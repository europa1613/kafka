package com.kafka.examples.avro;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class GenericAvroOrderConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", KafkaAvroDeserializer.class.getName());
        props.put("value.deserializer", KafkaAvroDeserializer.class.getName());
        props.put("group.id", "OrderGroup"); // Consumer group with scoped for each topic
        props.put("schema.registry.url", "http://localhost:8081"); // schema registry
        // props.put("specific.avro.reader", "true"); // Consumer group with scoped for each topic
        // NOT needed for GenericRecord

        try (KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("orders-gr-avro"));

            ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofSeconds(60));
            for (ConsumerRecord<String, GenericRecord> record : records) {
                System.out.println("customer: " + record.key());
                System.out.println("GenericRecord: " + record.value());
            }
        }
    }
}
