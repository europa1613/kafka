package com.kafka.examples.userproducer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProducerService {

//    private final KafkaTemplate<String, Integer> kafkaTemplate;

    private final KafkaTemplate<String, User> kafkaUserTemplate;

   /* public void sendUser(String name, int age) {
        kafkaTemplate.send("users", name , age);
    }*/

    public void sendUser(User user) {
        kafkaUserTemplate.send("users", user.getName(), user);
    }
}
