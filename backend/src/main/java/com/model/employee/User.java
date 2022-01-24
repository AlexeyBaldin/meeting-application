package com.model.employee;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="user_data")
@Data
public class User {

    @Id
    @Column(name = "employee_id")
    private int id;

    @Column(name = "user_login")
    private String login;

    @Column(name = "user_password")
    private long password;

    @Column(name = "user_activation")
    private boolean activation;

}
