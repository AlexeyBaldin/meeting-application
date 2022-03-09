package com.model.office;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name="office")
@Data
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "office_id")
    private int id;

    @Column(name = "office_city")
    private String city;

    @Column(name = "office_address")
    private String address;

    @Column(name = "office_phone")
    private String phone;

    @Column(name = "office_open")
    private LocalTime openTime;

    @Column(name = "office_close")
    private LocalTime closeTime;

}
