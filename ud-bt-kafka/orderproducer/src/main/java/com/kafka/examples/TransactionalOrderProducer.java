package com.kafka.examples;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class TransactionalOrderProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "order-producer-id");
        //props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "1000");

        try (KafkaProducer<String, Integer> producer = new KafkaProducer<>(props)) {
            producer.initTransactions();
            ProducerRecord<String, Integer> record1 = new ProducerRecord<>("orders", "MacBookPro", 10);
            ProducerRecord<String, Integer> record2 = new ProducerRecord<>("orders", "DellXPSPro", 5);
            //async send
            producer.beginTransaction();
            producer.send(record1, getCallback());
            producer.send(record2, getCallback());
            producer.commitTransaction();
            System.out.println("Message sent!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Callback getCallback() {
        return (metadata, e) -> {
            System.out.println(metadata);
            if (e != null) {
                System.out.println(e.getMessage());
            }
        };
    }
}
