package com.dto;

import com.model.meeting.Meeting;
import lombok.Data;

import java.util.List;


@Data
public class MeetingAndEmployeesIdDto {
    private Meeting meeting;
    private List<Integer> employeesId;
}
