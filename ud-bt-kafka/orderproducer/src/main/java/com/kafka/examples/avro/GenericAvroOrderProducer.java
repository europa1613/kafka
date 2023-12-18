package com.kafka.examples.avro;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class GenericAvroOrderProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", KafkaAvroSerializer.class.getName()); // Avro Serializer
        props.put("value.serializer", KafkaAvroSerializer.class.getName()); // Avro Serializer
        props.put("schema.registry.url", "http://localhost:8081"); // Schema registry

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse("{\n" +
                "  \"namespace\": \"com.kafka.examples.avro\",\n" +
                "  \"type\": \"record\",\n" +
                "  \"name\": \"Order\",\n" +
                "  \"fields\": [\n" +
                "    {\n" +
                "      \"name\": \"customer\",\n" +
                "      \"type\": \"string\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"product\",\n" +
                "      \"type\": \"string\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"quantity\",\n" +
                "      \"type\": \"int\"\n" +
                "    }\n" +
                "  ]\n" +
                "}");
        GenericRecord order = new GenericData.Record(schema);
        order.put("customer", "Jane");
        order.put("product", "iPad12.9");
        order.put("quantity", 11);

        try (KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("orders-gr-avro", order.get("customer")
                                                                                            .toString(), order);
            //async send
            producer.send(record, (metadata, e) -> {
                System.out.println("GenericAvroOrderProducer:.metadata: " + metadata);
                if (e != null) {
                    System.out.println("GenericAvroOrderProducer: Exception: " + e.getMessage());
                }
            });
            System.out.println("GenericAvroOrderProducer: Message sent!");
        } catch (Exception e) {
            System.out.println("GenericAvroOrderProducer: Exception2: " + e.getMessage());
        }
    }
}
