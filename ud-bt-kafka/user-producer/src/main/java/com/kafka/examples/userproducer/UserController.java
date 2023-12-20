package com.kafka.examples.userproducer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserProducerService service;

   /* @PostMapping("/users/{name}/{age}")
    public void sendUser(@PathVariable("name") String name, @PathVariable("age") int age) {
        service.sendUser(name, age);
    }*/

    @PostMapping("/users")
    public void sendUser(@RequestBody User user) {
        service.sendUser(user);
    }

}
