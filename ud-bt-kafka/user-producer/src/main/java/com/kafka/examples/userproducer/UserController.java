package com.kafka.examples.userproducer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserProducerService service;

    @PostMapping("/users/{name}/{age}")
    public void sendUser(@PathVariable("name") String name, @PathVariable("age") int age) {
        service.sendUser(name, age);
    }

}
