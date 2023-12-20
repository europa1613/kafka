package com.kafka.examples.userproducer;

import lombok.Data;

@Data
public class User {
    private String name;
    private int age;
    private String favGenre;
}
