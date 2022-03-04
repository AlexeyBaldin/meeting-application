package com.dto.query;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;

public interface MeetingWithInvite {

    @Value("#{target.meeting_id}")
    int getId();

    @Value("#{target.meeting_name}")
    String getName();

    @Value("#{target.room_id}")
    int getRoomId();

    @Value("#{target.office_id}")
    int getOfficeId();

    @Value("#{target.meeting_start}")
    Timestamp getStart();

    @Value("#{target.meeting_end}")
    Timestamp getEnd();

    @Value("#{target.invite_accept}")
    int getAccept();
}
