package com.kafka.examples.custom.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class OrderSerializer implements Serializer<Order> {
    @Override
    public byte[] serialize(String s, Order order) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsBytes(order);
        } catch (JsonProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
