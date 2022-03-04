package com.model.meeting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meeting")
@Data
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private int id;

    @Column(name = "meeting_name")
    private String name;

    @Column(name = "room_id")
    private int roomId;

    @Column(name = "office_id")
    private int officeId;

    @Column(name = "meeting_start")
    private Timestamp start;

    @Column(name = "meeting_end")
    private Timestamp end;

    @JsonIgnore
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Invite> invites;

    public Integer checkInviteAccept(Integer employeeId) {
        for (Invite invite : invites) {
            if (invite.getEmployeeId() == employeeId) {
                return invite.getInviteAccept();
            }
        }
        return null;
    }

    @JsonIgnore
    public List<Integer> getEmployeesId() {
        List<Integer> employeesId = new ArrayList<>();
        for (Invite invite : invites) {
            employeesId.add(invite.getEmployeeId());
        }
        return employeesId;
    }

    public void saveInvite(Integer employeeId) {
        invites.add(new Invite(employeeId, id, 0));
    }

    public Boolean activateInvite(Integer employeeId, Boolean accept) {
        for (Invite invite : invites) {
            if (invite.getEmployeeId() == employeeId) {
                invite.setInviteAccept(accept ? 1 : 2);
                return true;
            }
        }
        return null;
    }

    public Boolean deleteInvite(Integer employeeId) {
        for (Invite invite : invites) {
            if (invite.getEmployeeId() == employeeId) {
                return invites.remove(invite);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roomId=" + roomId +
                ", officeId=" + officeId +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
