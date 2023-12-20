package com.kafka.examples.userconsumer;

import com.kafka.examples.userproducer.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserConsumerService {

    @KafkaListener(topics = {"users"})
    public void consumeUser(User user) {
        log.info("User: {}", user);
    }
}
