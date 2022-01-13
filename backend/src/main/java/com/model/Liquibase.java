package com.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "liquibase")
@Data
public class Liquibase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "lname")
    private String name;

}
