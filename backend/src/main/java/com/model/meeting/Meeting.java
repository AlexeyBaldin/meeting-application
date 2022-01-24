package com.model.meeting;

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

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Invite> invites;

    public Boolean checkInviteAccept(Integer employeeId) {
        for (Invite invite : invites) {
            if (invite.getEmployeeId() == employeeId) {
                return invite.isInviteAccept();
            }
        }
        return null;
    }

    public List<Integer> getEmployeesId() {
        List<Integer> employeesId = new ArrayList<>();
        for (Invite invite : invites) {
            employeesId.add(invite.getEmployeeId());
        }
        return employeesId;
    }

    public void saveInvite(Integer employeeId) {
        invites.add(new Invite(employeeId, id, false));
    }

    public Boolean acceptInvite(Integer employeeId) {
        for (Invite invite : invites) {
            if (invite.getEmployeeId() == employeeId) {
                invite.setInviteAccept(true);
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
}
