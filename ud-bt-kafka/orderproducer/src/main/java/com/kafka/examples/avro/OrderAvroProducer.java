package com.kafka.examples.avro;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class OrderAvroProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", KafkaAvroSerializer.class.getName()); // Avro Serializer
        props.put("value.serializer", KafkaAvroSerializer.class.getName()); // Avro Serializer
        props.put("schema.registry.url", "http://localhost:8081"); // Schema registry

        Order order = new Order("Doe, John", "iPhone", 5);

        try (KafkaProducer<String, Order> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, Order> record = new ProducerRecord<>("orders-avro", order.getCustomer()
                                                                                            .toString(), order); // String types are CharSequence
            //async send
            producer.send(record, (metadata, e) -> {
                System.out.println("OrderAvroProducer:.metadata: " + metadata);
                if (e != null) {
                    System.out.println("OrderAvroProducer: Exception: " + e.getMessage());
                }
            });
            System.out.println("OrderAvroProducer: Message sent!");
        } catch (Exception e) {
            System.out.println("OrderAvroProducer: Exception2: " + e.getMessage());
        }
    }
}
