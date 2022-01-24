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

    @ManyToMany
    @JoinTable(name = "employee_role",
               joinColumns = @JoinColumn(name = "employee_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> employeeRole;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Invite> invites;


    public Boolean checkRole(Role role) {
        return employeeRole.contains(role);
    }

    public void addRole(Role role) {
        employeeRole.add(role);
    }

    public void deleteRole(Role role) {
        employeeRole.remove(role);
    }
}
