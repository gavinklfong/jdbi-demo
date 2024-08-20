package com.example.demo.model;

import lombok.Value;

@Value
public class Customer {
    String id;
    String name;
    String email;
    Integer tier;
}
