package com.model.meeting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.model.employee.Employee;
import lombok.Data;

import javax.persistence.*;

@Entity
@IdClass(InviteId.class)
@Table(name="invite")
@Data
public class Invite {

    @Id
    @Column(name = "employee_id")
    private int employeeId;

    @Id
    @Column(name = "meeting_id")
    private int meetingId;

    @Column(name = "invite_accept")
    private Integer inviteAccept;

    @JsonIgnore
    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @JsonIgnore
    @ManyToOne
    @MapsId("meetingId")
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    public Invite() {}

    public Invite(int employeeId, int meetingId, Integer inviteAccept) {
        this.employeeId = employeeId;
        this.meetingId = meetingId;
        this.inviteAccept = inviteAccept;
    }
}
