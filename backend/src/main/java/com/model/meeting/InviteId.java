package com.model.meeting;

import lombok.Data;

import java.io.Serializable;

@Data
public class InviteId implements Serializable {

    private int employeeId;

    private int meetingId;

    public InviteId() {}

    public InviteId(int employeeId, int meetingId) {
        this.employeeId = employeeId;
        this.meetingId = meetingId;
    }
}
