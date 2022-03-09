package com.controller;

import com.dto.MeetingAndEmployeesIdDto;
import com.dto.query.ItemInRoom;
import com.dto.query.MeetingWithInvite;
import com.model.employee.Employee;
import com.model.employee.User;
import com.model.meeting.Meeting;
import com.model.office.Office;
import com.model.office.Room;
import com.service.employee.EmployeeService;
import com.service.employee.UserService;
import com.service.meeting.MeetingService;
import com.service.office.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/forall/rest")
public class ForAllRestController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    MeetingService meetingService;

    @Autowired
    OfficeService officeService;

    @Autowired
    UserService userService;

    @GetMapping("/employee/all")
    public List<Employee> findAll() {
        return employeeService.findAllEmployees();
    }

    @GetMapping("/employee/{employee_id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable(value = "employee_id") Integer employeeId) {
        if(employeeService.isEmployeeExist(employeeId)) {
            Employee employee = employeeService.findEmployeeById(employeeId);
            return ResponseEntity.ok().body(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/meeting/all")
    public List<Meeting> findAllMeetings() {
        return meetingService.findAllMeetings();
    }

    @GetMapping("/meeting/all/{employee_id}")
    public ResponseEntity<List<MeetingWithInvite>> findAllEmployeeMeetingsWithInvite(@PathVariable(value = "employee_id") Integer employeeId) {
        if(employeeService.isEmployeeExist(employeeId)) {
            List<MeetingWithInvite> meetingWithInvites = meetingService.findAllEmployeeMeetingsWithInvite(employeeId);
            return ResponseEntity.ok().body(meetingWithInvites);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/meeting/{meeting_id}/employees")
    public ResponseEntity<List<Employee>> findAllEmployeesByMeetingId(@PathVariable(value = "meeting_id") Integer meetingId) {
        if(meetingService.isMeetingExists(meetingId)) {
            List<Employee> employees = meetingService.findAllEmployeesByMeetingId(meetingId);
            return ResponseEntity.ok().body(employees);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/meeting/room/{room_id}")
    public ResponseEntity<List<Meeting>> findAllByRoomId(@PathVariable(value = "room_id") Integer roomId) {
        if(officeService.isRoomExists(roomId)) {
            List<Meeting> meetings = meetingService.findAllByRoomId(roomId);
            return ResponseEntity.ok().body(meetings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/meeting/save")
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

    @PutMapping("/meeting/invite/{employee_id}/{meeting_id}")
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

    @GetMapping("/office/all")
    public List<Office> findAllOffices() {
        return officeService.findAllOffices();
    }

    @GetMapping("/office/{office_id}")
    public ResponseEntity<Office> findById(@PathVariable(value = "office_id") Integer officeId) {
        if(officeService.isOfficeExists(officeId)) {
            Office office = officeService.findOfficeById(officeId);
            return ResponseEntity.ok().body(office);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/office/{office_id}/room")
    public ResponseEntity<List<Room>> findAllRoomsInOffice(@PathVariable(value = "office_id") Integer officeId) {
        if(officeService.isOfficeExists(officeId)) {
            List<Room> rooms = officeService.findAllRoomsInOffice(officeId);
            return ResponseEntity.ok().body(rooms);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/office/room/{room_id}")
    public ResponseEntity<Room> findRoomByRoomId(@PathVariable(value = "room_id") Integer roomId) {
        if(officeService.isRoomExists(roomId)) {
            Room room = officeService.findRoomByRoomId(roomId);
            return ResponseEntity.ok().body(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/office/room/{room_id}/item/all")
    public ResponseEntity<List<ItemInRoom>> findAllItemsInRoom(@PathVariable(value = "room_id") Integer roomId) {
        if(officeService.isRoomExists(roomId)) {
            return ResponseEntity.ok().body(officeService.findAllItemsInRoom(roomId));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user/{user_id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable(value = "user_id") Integer userId, @RequestBody User newUser) {

        if(userService.isUserExists(userId)) {

            newUser.setId(userId);
            Map<String, Object> responseMap = userService.checkUserAndGetErrorsMap(newUser);

            if(responseMap.isEmpty()) {
                userService.updateUser(userId, newUser);
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

}
