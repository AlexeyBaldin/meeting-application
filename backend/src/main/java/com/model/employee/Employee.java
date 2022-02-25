package com.model.employee;


import com.model.meeting.Invite;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="employee")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int id;

    @Column(name = "office_id")
    private int officeId;

    @Column(name = "employee_name")
    private String name;

    @Column(name = "employee_position")
    private String position;

    @Column(name = "employee_email")
    private String email;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Invite> invites;

}
