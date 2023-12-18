package com.kafka.examples.custom.serializers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class OrderCSProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "com.kafka.examples.custom.serializers.OrderSerializer");

        Order order = new Order("Doe, John", "iPhone", 5);

        try (KafkaProducer<String, Order> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, Order> record = new ProducerRecord<>("orders-cs", order.getCustomer(), order);
            //async send
            producer.send(record, (metadata, e) -> {
                System.out.println("OrderCSProduce:.metadata: " + metadata);
                if (e != null) {
                    System.out.println("OrderCSProducer: Exception: " + e.getMessage());
                }
            });
            System.out.println("OrderCSProducer: Message sent!");
        } catch (Exception e) {
            System.out.println("OrderCSProducer: Exception2: " + e.getMessage());
        }
    }
}
