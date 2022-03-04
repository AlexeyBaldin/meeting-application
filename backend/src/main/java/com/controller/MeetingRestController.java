package com.controller;

import com.dto.MeetingAndEmployeesIdDto;
import com.dto.query.MeetingWithInvite;
import com.model.meeting.Meeting;
import com.service.employee.EmployeeService;
import com.service.meeting.MeetingService;
import com.service.office.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/meeting")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    OfficeService officeService;

    @GetMapping("/all")
    public List<Meeting> findAll() {
        return meetingService.findAllMeetings();
    }

    @GetMapping("/{meeting_id}")
    public ResponseEntity<Meeting> findMeetingById(@PathVariable(value = "meeting_id") Integer meetingId) {
        if(meetingService.isMeetingExists(meetingId)) {
            Meeting meeting = meetingService.findMeetingById(meetingId);
            return ResponseEntity.ok().body(meeting);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all/{employee_id}")
    public ResponseEntity<List<MeetingWithInvite>> findAllEmployeeMeetingsWithInvite(@PathVariable(value = "employee_id") Integer employeeId) {
        if(employeeService.isEmployeeExist(employeeId)) {
            List<MeetingWithInvite> meetingWithInvites = meetingService.findAllEmployeeMeetingsWithInvite(employeeId);
            return ResponseEntity.ok().body(meetingWithInvites);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/invite/{employee_id}/{meeting_id}")
    public ResponseEntity<Map<String, Object>> checkInviteAccept(@PathVariable(value = "employee_id") Integer employeeId,
                                                                 @PathVariable(value = "meeting_id") Integer meetingId) {

        if(meetingService.isMeetingExists(meetingId) && employeeService.isEmployeeExist(employeeId)) {
            Integer check = meetingService.checkInviteAccept(employeeId, meetingId);
            Map<String, Object> responseMap = new HashMap<>();
            if(check != null) {
                responseMap.put("success", true);
                responseMap.put("accept", check);

                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                return ResponseEntity.badRequest().body(responseMap);
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{meeting_id}/employees")
    public ResponseEntity<List<Integer>> findAllEmployeesIdByMeetingId(@PathVariable(value = "meeting_id") Integer meetingId) {
        if(meetingService.isMeetingExists(meetingId)) {
            List<Integer> employeesId = meetingService.findAllEmployeesIdByMeetingId(meetingId);
            return ResponseEntity.ok().body(employeesId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveMeeting(@RequestBody MeetingAndEmployeesIdDto meetingAndEmployeesIdDto) {

        if(meetingAndEmployeesIdDto.getEmployeesId() != null) {
            for (Integer employeeId : meetingAndEmployeesIdDto.getEmployeesId()) {
                if (!employeeService.isEmployeeExist(employeeId)) {
                    return ResponseEntity.notFound().build();
                }
            }
        }

        Map<String, Object> responseMap = meetingService.checkMeetingAndGetErrorsMap(meetingAndEmployeesIdDto.getMeeting(), meetingAndEmployeesIdDto.getEmployeesId());

        if(responseMap.isEmpty()) {
            Meeting meeting = meetingService.saveMeeting(meetingAndEmployeesIdDto.getMeeting());
            if(meetingAndEmployeesIdDto.getEmployeesId() != null) {
                meetingService.saveInvites(meetingAndEmployeesIdDto.getEmployeesId(), meeting.getId());
            }

            responseMap.put("success", true);
            return ResponseEntity.ok().body(responseMap);
        } else {
            responseMap.put("success", false);
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @PostMapping("/invite/{employee_id}/{meeting_id}")
    public ResponseEntity<Map<String, Object>> saveInvite(@PathVariable(value = "employee_id") Integer employeeId,
                                                          @PathVariable(value = "meeting_id") Integer meetingId) {

        if(meetingService.isMeetingExists(meetingId) && employeeService.isEmployeeExist(employeeId)) {
            Map<String, Object> responseMap = meetingService.checkInviteAndGetErrorsMap(employeeId, meetingId);

            if(responseMap.isEmpty()) {
                meetingService.saveInvite(employeeId, meetingId);
                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                return ResponseEntity.badRequest().body(responseMap);
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{meeting_id}")
    public ResponseEntity<Map<String, Object>> updateMeeting(@PathVariable(value = "meeting_id") Integer meetingId,
                                                             @RequestBody Meeting newMeeting) {

        if(meetingService.isMeetingExists(meetingId)) {

            Map<String, Object> responseMap = meetingService.checkMeetingAndGetErrorsMap(newMeeting, null);

            if(responseMap.isEmpty()) {
                meetingService.updateMeeting(meetingId, newMeeting);

                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                return ResponseEntity.badRequest().body(responseMap);
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/invite/{employee_id}/{meeting_id}")
    public ResponseEntity<Map<String, Object>> activateInvite(@PathVariable(value = "employee_id") Integer employeeId,
                                                              @PathVariable(value = "meeting_id") Integer meetingId,
                                                              @RequestBody Boolean accept) {
        if(meetingService.isMeetingExists(meetingId) && employeeService.isEmployeeExist(employeeId)) {
            Integer check = meetingService.checkInviteAccept(employeeId, meetingId);


            Map<String, Object> responseMap = new HashMap<>();
            if(check == null) {
                responseMap.put("success", false);
                responseMap.put("invite error", "invite does`t exists for employeeId = " + employeeId + " and meetingId = " + meetingId);

                return ResponseEntity.badRequest().body(responseMap);
            } else {

                String error = meetingService.activateInviteAndGetError(employeeId, meetingId, accept);

                if(error != null) {
                    responseMap.put("success", false);
                    responseMap.put("employee error", error);

                    return ResponseEntity.badRequest().body(responseMap);
                }

                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{meeting_id}")
    public ResponseEntity<Map<String, Object>> deleteMeeting(@PathVariable(value = "meeting_id") Integer meetingId) {

        if(meetingService.isMeetingExists(meetingId)) {
            meetingService.deleteMeeting(meetingId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);

            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/invite/{employee_id}/{meeting_id}")
    public ResponseEntity<Map<String, Object>> deleteInvite(@PathVariable(value = "employee_id") Integer employeeId,
                                                            @PathVariable(value = "meeting_id") Integer meetingId) {

        if(meetingService.isMeetingExists(meetingId) && employeeService.isEmployeeExist(employeeId)) {
            Integer check = meetingService.checkInviteAccept(employeeId, meetingId);

            Map<String, Object> responseMap = new HashMap<>();
            if(check != null) {
                meetingService.deleteInvite(employeeId, meetingId);

                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                responseMap.put("invite error", "invite does`t exists for employeeId = " + employeeId + " and meetingId = " + meetingId);

                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
