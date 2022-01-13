package com.model;

import lombok.Data;

@Data
public class ForTest {

    private int id;
    private String name;
    private int age;

    public ForTest(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
