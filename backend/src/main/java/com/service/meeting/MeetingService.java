package com.service.meeting;

import com.dto.query.MeetingWithInvite;
import com.model.employee.Employee;
import com.model.meeting.Meeting;
import com.model.office.Office;
import com.model.office.Room;
import com.repository.employee.EmployeeRepository;
import com.repository.meeting.MeetingRepository;
import com.service.employee.EmployeeService;
import com.service.office.OfficeService;
import com.util.FieldChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeetingService {

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    OfficeService officeService;

    public List<Meeting> findAllMeetings() {
        return meetingRepository.findAll();
    }

    public Boolean isMeetingExists(Integer meetingId) {
        return meetingRepository.existsById(meetingId);
    }

    public Meeting findMeetingById(Integer meetingId) {
        return meetingRepository.findById(meetingId).orElse(null);
    }

    public Meeting saveMeeting(Meeting newMeeting) {
        if(newMeeting.getInvites() == null) {
            newMeeting.setInvites(new ArrayList<>());
        }

        return meetingRepository.save(newMeeting);
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

    public List<Employee> findAllEmployeesByMeetingId(Integer meetingId) {
        return employeeRepository.findAllEmployeeByMeetingId(meetingId);
    }

    public List<MeetingWithInvite> findAllEmployeeMeetingsWithInvite(Integer employeeId) {
        return meetingRepository.findAllEmployeeMeetingsWithInvite(employeeId);
    }

    public void saveInvite(Integer employeeId, Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);

        meeting.saveInvite(employeeId);
        saveMeeting(meeting);
    }

    public void saveInvites(List<Integer> employeesId, Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);

        employeesId.forEach(meeting::saveInvite);
        saveMeeting(meeting);
    }

    public String checkEmployeeTimeAndGetError(Integer employeeId, Integer meetingId, Timestamp start, Timestamp end) {
        List<Meeting> meetings = meetingRepository.findAllEmployeeAcceptedMeetings(employeeId, meetingId);

        return checkIntersectsAndGetError(meetings, start, end, "Employee meeting intersects with meetings with ids: ");
    }

    public String activateInviteAndGetError(Integer employeeId, Integer meetingId, Boolean accept) {
        Meeting meeting = findMeetingById(meetingId);

        if(!accept) {
            meeting.activateInvite(employeeId, false);
            saveMeeting(meeting);
        } else {
            String error = checkEmployeeTimeAndGetError(employeeId, meetingId, meeting.getStart(), meeting.getEnd());
            if(error == null) {
                meeting.activateInvite(employeeId, true);
                saveMeeting(meeting);
            } else {
                return error;
            }
        }
        return null;
    }

    public void deleteInvite(Integer employeeId, Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);
        meeting.deleteInvite(employeeId);
        saveMeeting(meeting);
    }

    private String checkExistsInvite(Integer employeeId, Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);

        Integer checkInvite = meeting.checkInviteAccept(employeeId);
        if(checkInvite != null) {
            return "invite already exists for employeeId = " + employeeId + " and meetingId = " + meetingId;
        }
        return null;
    }

    public Integer checkInviteAccept(Integer employeeId, Integer meetingId) {
        Meeting meeting = findMeetingById(meetingId);

        return meeting.checkInviteAccept(employeeId);
    }

    private String checkRoomCapacityAndGetError(Integer roomId, Integer invitesCount) {
        Room room = officeService.findRoomById(roomId);

        if(room.getCapacity() < invitesCount) {
            return "room capacity (" + room.getCapacity() + ") < invites count (" + invitesCount + ")";
        }
        return null;
    }

    public Map<String, Object> checkInviteAndGetErrorsMap(Integer employeeId, Integer meetingId) {
        Map<String, Object> errors = new HashMap<>();

        String check = checkExistsInvite(employeeId, meetingId);
        if(check != null) {
            errors.put("exist invite error", check);
        }

        Meeting meeting = findMeetingById(meetingId);
        check = checkRoomCapacityAndGetError(meeting.getRoomId(), meeting.getInvites().size() + 1);
        if(check != null) {
            errors.put("room capacity error", check);
        }

        return errors;
    }

    private String checkMeetingNameAndGetError(String meetingName) {
        return FieldChecker.checkNullStringAndGetError(meetingName);
    }

    private String checkStartTimeAndGetError(Timestamp start) {
        return FieldChecker.checkNullTimestampAndGetError(start);
    }

    private String checkEndTimeAndGetError(Timestamp end) {
        return FieldChecker.checkNullTimestampAndGetError(end);
    }

    private String checkOfficeTimeAndGetError(Integer officeId, Timestamp start, Timestamp end) {
        Office office = officeService.findOfficeById(officeId);

        Instant instant = Instant.now();
        int offsetSeconds = ZoneId.systemDefault().getRules().getStandardOffset(instant).getTotalSeconds();

        LocalTime startTime = start.toLocalDateTime().toLocalTime().minusSeconds(offsetSeconds);
        LocalTime endTime = end.toLocalDateTime().toLocalTime().minusSeconds(offsetSeconds);


        if(!(startTime.isAfter(office.getOpenTime()) && startTime.isBefore(office.getCloseTime())) ||
                !(endTime.isAfter(office.getOpenTime()) && endTime.isBefore(office.getCloseTime()))) {
            return "the office is closed during this period of time (or part of period)";
        }

        return null;
    }

    private Map<String, Object> checkTimeAndGetErrorsMap(Integer officeId, Timestamp start, Timestamp end) {
        Map<String, Object> errors = new HashMap<>();

        String check = checkStartTimeAndGetError(start);
        if(check != null) {
            errors.put("field(start) error", check);
        }

        check = checkEndTimeAndGetError(end);
        if(check != null) {
            errors.put("field(end) error", check);
        }

        if(start != null && end != null) {
            if(start.getTime() <= new Timestamp(System.currentTimeMillis()).getTime()) {
                errors.put("start time error", "start time (" + start + ") <= current time");
            }

            if(end.getTime() <= new Timestamp(System.currentTimeMillis()).getTime()) {
                errors.put("end time error", "end time (" + end + ") <= current time");
            }

            if(start.getTime() >= end.getTime()) {
                errors.put("time position error", "start time (" + start + ") >= end time (" + end + ")");
            }

            check = checkOfficeTimeAndGetError(officeId, start, end);
            if(check != null) {
                errors.put("office time error", check);
            }
        }

        return errors;
    }

    private String checkRoomAndGetError(Integer meetingId, Integer roomId, Timestamp start, Timestamp end) {
        List<Meeting> meetings = meetingRepository.findAllByRoomIdWithInvite(roomId, meetingId);

        return checkIntersectsAndGetError(meetings, start, end, "Meeting time intersects with meetings with ids: ");
    }

    private String checkIntersectsAndGetError(List<Meeting> meetings, Timestamp start, Timestamp end, String startedString) {
        StringBuilder error = new StringBuilder(startedString);
        int intersects = 0;
        if(!meetings.isEmpty()) {
            for (Meeting meeting : meetings) {
                if(!(meeting.getEnd().getTime() <= start.getTime() || meeting.getStart().getTime() >= end.getTime())) {
                    error.append(meeting.getId()).append(" ");
                    intersects++;
                }
            }
        }

        if(intersects != 0) {
            return error.toString().trim();
        } else {
            return null;
        }
    }

    public Map<String, Object> checkMeetingAndGetErrorsMap(Meeting newMeeting, List<Integer> employeesId) {
        Map<String, Object> errors = new HashMap<>();

        System.out.println(newMeeting.getStart());

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

        errors.putAll(checkTimeAndGetErrorsMap(newMeeting.getOfficeId(), newMeeting.getStart(), newMeeting.getEnd()));

        if(newMeeting.getStart() != null && newMeeting.getEnd() != null) {
            check = checkRoomAndGetError(newMeeting.getId(), newMeeting.getRoomId(), newMeeting.getStart(), newMeeting.getEnd());
            if(check != null) {
                errors.put("room time error", check);
            }
        }

        if(employeesId != null) {
            check = checkRoomCapacityAndGetError(newMeeting.getRoomId(), employeesId.size());
            if(check != null) {
                errors.put("room capacity error", check);
            }
        }

        return errors;
    }

    public List<Meeting> findAllByRoomId(Integer roomId) {
        return meetingRepository.findAllByRoomId(roomId);
    }
}
