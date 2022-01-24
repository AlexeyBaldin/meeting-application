package com.service.meeting;

import com.model.meeting.Meeting;
import com.repository.meeting.MeetingRepository;
import com.service.CheckService;
import com.service.employee.EmployeeService;
import com.service.office.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeetingService {

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    OfficeService officeService;

    @Autowired
    CheckService checkService;

    public List<Meeting> findAllMeetings() {
        return meetingRepository.findAll();
    }

    public Boolean isMeetingExists(Integer meetingId) {
        return meetingRepository.existsById(meetingId);
    }

    public Meeting findMeetingById(Integer meetingId) {
        return meetingRepository.findById(meetingId).orElse(null);
    }

    public void saveMeeting(Meeting newMeeting) {
        meetingRepository.save(newMeeting);
    }

    public void updateMeeting(Integer meetingId, Meeting newMeeting) {
        Meeting meeting = findMeetingById(meetingId);

        newMeeting.setId(meetingId);
        newMeeting.setInvites(meeting.getInvites());

        meetingRepository.save(newMeeting);
    }

    public void deleteMeeting(Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);

        meetingRepository.delete(meeting);
    }

    public List<Integer> findAllEmployeesIdByMeetingId(Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);
        return meeting.getEmployeesId();
    }

    public void saveInvite(Integer employeeId, Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);

        meeting.saveInvite(employeeId);
        saveMeeting(meeting);
    }

    public void acceptInvite(Integer employeeId, Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);
        meeting.acceptInvite(employeeId);
        saveMeeting(meeting);
    }

    public void deleteInvite(Integer employeeId, Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);
        meeting.deleteInvite(employeeId);
        saveMeeting(meeting);
    }

    public Boolean checkExistsInvite(Integer employeeId, Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);
        return meeting.checkInviteAccept(employeeId);
    }

    private String checkMeetingNameAndGetError(String meetingName) {
        return checkService.checkNullStringAndGetError(meetingName);
    }

    private String checkStartTimeAndGetError(Timestamp start) {
        return checkService.checkNullTimestampAndGetError(start);
    }

    private String checkEndTimeAndGetError(Timestamp end) {
        return checkService.checkNullTimestampAndGetError(end);
    }

    public Map<String, Object> checkMeetingAndGetErrorsMap(Meeting newMeeting) {
        Map<String, Object> errors = new HashMap<>();

        if(!officeService.isOfficeExists(newMeeting.getOfficeId())) {
            errors.put("field(officeId) error", "office with id = " + newMeeting.getOfficeId() + " does`t exist");
        }

        if(!officeService.isRoomExists(newMeeting.getRoomId())) {
            errors.put("field(roomId) error", "room with id = " + newMeeting.getRoomId() + " does`t exist");
        }

        String check = checkMeetingNameAndGetError(newMeeting.getName());
        if(check != null) {
            errors.put("field(name) error", check);
        }

        check = checkStartTimeAndGetError(newMeeting.getStart());
        if(check != null) {
            errors.put("field(start) error", check);
        }

        check = checkEndTimeAndGetError(newMeeting.getEnd());
        if(check != null) {
            errors.put("field(end) error", check);
        }

        return errors;
    }
}
