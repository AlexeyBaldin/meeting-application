package com.validation;


import com.model.meeting.Meeting;
import com.model.office.Office;
import com.model.office.Room;
import com.repository.meeting.MeetingRepository;
import com.service.meeting.MeetingService;
import com.service.office.OfficeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MeetingValidationTest {

    @Mock
    OfficeService officeService;

    @Mock
    MeetingRepository meetingRepository;

    @InjectMocks
    MeetingService meetingService;

    static class MeetingValidationTestObject {
        List<Meeting> meetingList;
        Meeting meeting;
        Meeting meeting2;
        Meeting meeting3;
        Office office;
        Room room;
        List<Integer> employeesId;
    }

    MeetingValidationTestObject object = new MeetingValidationTestObject();

    @Before
    public void before() {
        List<Meeting> meetingList = new ArrayList<>();

        Meeting meeting = new Meeting();
        meeting.setId(1);
        meeting.setName("Test");
        meeting.setOfficeId(1);
        meeting.setRoomId(1);
        meeting.setStart(new Timestamp(150, 2, 1, 15, 0, 0, 0));
        meeting.setEnd(new Timestamp(150, 2, 1, 16, 0, 0, 0));
        meeting.setInvites(new ArrayList<>());

        Meeting meeting2 = new Meeting();
        meeting2.setId(2);
        meeting2.setName("Test2");
        meeting2.setOfficeId(1);
        meeting2.setRoomId(1);
        meeting2.setStart(new Timestamp(150, 2, 1, 17, 0, 0, 0));
        meeting2.setEnd(new Timestamp(150, 2, 1, 18, 0, 0, 0));
        meeting2.setInvites(new ArrayList<>());

        Meeting meeting3 = new Meeting();
        meeting3.setId(3);
        meeting3.setName("Test3");
        meeting3.setOfficeId(1);
        meeting3.setRoomId(1);
        meeting3.setStart(new Timestamp(150, 2, 2, 15, 0, 0, 0));
        meeting3.setEnd(new Timestamp(150, 2, 2, 16, 0, 0, 0));
        meeting3.setInvites(new ArrayList<>());

        meetingList.add(meeting2);
        meetingList.add(meeting3);

        Office office = new Office();
        office.setId(1);
        office.setAddress("Address");
        office.setCity("City");
        office.setPhone("88005553535");
        office.setOpenTime(LocalTime.of(6, 0));
        office.setCloseTime(LocalTime.of(22, 0));

        Room room = new Room();
        room.setId(1);
        room.setOfficeId(1);
        room.setCabinet(10);
        room.setCapacity(10);

        List<Integer> employeesId = new ArrayList<>();
        employeesId.add(1);
        employeesId.add(2);
        employeesId.add(3);

        object.meeting = meeting;
        object.meeting2 = meeting2;
        object.meeting3 = meeting3;
        object.meetingList = meetingList;
        object.office = office;
        object.room = room;
        object.employeesId = employeesId;
    }

    @Test
    public void testCheckMeetingSuccess() {
        when(officeService.isOfficeExists(1)).thenReturn(true);
        when(officeService.isRoomExists(1)).thenReturn(true);
        when(officeService.findOfficeById(1)).thenReturn(object.office);
        when(meetingRepository.findAllByRoomIdWithInvite(1, 1)).thenReturn(object.meetingList);
        when(officeService.findRoomById(1)).thenReturn(object.room);

        Map<String, Object> result = meetingService.checkMeetingAndGetErrorsMap(object.meeting, object.employeesId);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckMeetingOfficeNotExist() {
        when(officeService.isOfficeExists(1)).thenReturn(false);
        when(officeService.isRoomExists(1)).thenReturn(true);
        when(officeService.findOfficeById(1)).thenReturn(object.office);
        when(meetingRepository.findAllByRoomIdWithInvite(1, 1)).thenReturn(object.meetingList);
        when(officeService.findRoomById(1)).thenReturn(object.room);

        Map<String, Object> result = meetingService.checkMeetingAndGetErrorsMap(object.meeting, object.employeesId);

        Assert.assertTrue(result.containsKey("field(officeId) error"));
    }

    @Test
    public void testCheckMeetingRoomNotExist() {
        when(officeService.isOfficeExists(1)).thenReturn(true);
        when(officeService.isRoomExists(1)).thenReturn(false);
        when(officeService.findOfficeById(1)).thenReturn(object.office);
        when(meetingRepository.findAllByRoomIdWithInvite(1, 1)).thenReturn(object.meetingList);
        when(officeService.findRoomById(1)).thenReturn(object.room);

        Map<String, Object> result = meetingService.checkMeetingAndGetErrorsMap(object.meeting, object.employeesId);

        Assert.assertTrue(result.containsKey("field(roomId) error"));
    }

    @Test
    public void testCheckMeetingStartTimeBeforeNow() {
        when(officeService.isOfficeExists(1)).thenReturn(true);
        when(officeService.isRoomExists(1)).thenReturn(true);
        when(officeService.findOfficeById(1)).thenReturn(object.office);
        when(meetingRepository.findAllByRoomIdWithInvite(1, 1)).thenReturn(object.meetingList);
        when(officeService.findRoomById(1)).thenReturn(object.room);

        object.meeting.setStart(new Timestamp(100, 0, 0, 0, 0, 0, 0));

        Map<String, Object> result = meetingService.checkMeetingAndGetErrorsMap(object.meeting, object.employeesId);

        Assert.assertTrue(result.containsKey("start time error"));
    }

    @Test
    public void testCheckMeetingEndTimeBeforeNow() {
        when(officeService.isOfficeExists(1)).thenReturn(true);
        when(officeService.isRoomExists(1)).thenReturn(true);
        when(officeService.findOfficeById(1)).thenReturn(object.office);
        when(meetingRepository.findAllByRoomIdWithInvite(1, 1)).thenReturn(object.meetingList);
        when(officeService.findRoomById(1)).thenReturn(object.room);

        object.meeting.setEnd(new Timestamp(100, 0, 0, 0, 0, 0, 0));

        Map<String, Object> result = meetingService.checkMeetingAndGetErrorsMap(object.meeting, object.employeesId);

        Assert.assertTrue(result.containsKey("end time error"));
    }

    @Test
    public void testCheckMeetingTimePositionError() {
        when(officeService.isOfficeExists(1)).thenReturn(true);
        when(officeService.isRoomExists(1)).thenReturn(true);
        when(officeService.findOfficeById(1)).thenReturn(object.office);
        when(meetingRepository.findAllByRoomIdWithInvite(1, 1)).thenReturn(object.meetingList);
        when(officeService.findRoomById(1)).thenReturn(object.room);


        object.meeting.setEnd(new Timestamp(125, 0, 0, 15, 0, 0, 0));
        object.meeting.setStart(new Timestamp(125, 0, 0, 16, 0, 0, 0));

        Map<String, Object> result = meetingService.checkMeetingAndGetErrorsMap(object.meeting, object.employeesId);

        Assert.assertTrue(result.containsKey("time position error"));
    }

    @Test
    public void testCheckMeetingOfficeTimeError() {
        when(officeService.isOfficeExists(1)).thenReturn(true);
        when(officeService.isRoomExists(1)).thenReturn(true);
        when(officeService.findOfficeById(1)).thenReturn(object.office);
        when(meetingRepository.findAllByRoomIdWithInvite(1, 1)).thenReturn(object.meetingList);
        when(officeService.findRoomById(1)).thenReturn(object.room);


        object.meeting.setEnd(new Timestamp(125, 0, 0, 3, 0, 0, 0));
        object.meeting.setStart(new Timestamp(125, 0, 0, 2, 0, 0, 0));

        Map<String, Object> result = meetingService.checkMeetingAndGetErrorsMap(object.meeting, object.employeesId);

        Assert.assertTrue(result.containsKey("office time error"));
    }

    @Test
    public void testCheckMeetingRoomIntersects() {
        when(officeService.isOfficeExists(1)).thenReturn(true);
        when(officeService.isRoomExists(1)).thenReturn(true);
        when(officeService.findOfficeById(1)).thenReturn(object.office);
        when(meetingRepository.findAllByRoomIdWithInvite(1, 1)).thenReturn(object.meetingList);
        when(officeService.findRoomById(1)).thenReturn(object.room);

        object.meeting2.setStart(new Timestamp(150, 2, 1, 15, 0, 0, 0));
        object.meeting2.setEnd(new Timestamp(150, 2, 1, 16, 0, 0, 0));

        Map<String, Object> result = meetingService.checkMeetingAndGetErrorsMap(object.meeting, object.employeesId);

        Assert.assertTrue(result.containsKey("room time error"));
    }

    @Test
    public void testCheckMeetingRoomCapacityError() {
        when(officeService.isOfficeExists(1)).thenReturn(true);
        when(officeService.isRoomExists(1)).thenReturn(true);
        when(officeService.findOfficeById(1)).thenReturn(object.office);
        when(meetingRepository.findAllByRoomIdWithInvite(1, 1)).thenReturn(object.meetingList);
        when(officeService.findRoomById(1)).thenReturn(object.room);

        object.room.setCapacity(1);

        Map<String, Object> result = meetingService.checkMeetingAndGetErrorsMap(object.meeting, object.employeesId);

        Assert.assertTrue(result.containsKey("room capacity error"));
    }

    @Test
    public void testCheckMeetingInviteSuccess() {
        when(meetingRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(object.meeting));
        when(officeService.findRoomById(1)).thenReturn(object.room);

        Map<String, Object> result = meetingService.checkInviteAndGetErrorsMap(1, 1);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckMeetingInviteAlreadyExist() {
        when(meetingRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(object.meeting));
        when(officeService.findRoomById(1)).thenReturn(object.room);

        object.meeting.saveInvite(1);

        Map<String, Object> result = meetingService.checkInviteAndGetErrorsMap(1, 1);

        Assert.assertTrue(result.containsKey("exist invite error"));
    }

    @Test
    public void testCheckMeetingInviteRoomCapacityError() {
        when(meetingRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(object.meeting));
        when(officeService.findRoomById(1)).thenReturn(object.room);

        object.meeting.saveInvite(1);
        object.room.setCapacity(1);

        Map<String, Object> result = meetingService.checkInviteAndGetErrorsMap(2, 1);

        Assert.assertTrue(result.containsKey("room capacity error"));
    }

    @Test
    public void testCheckMeetingInviteActivateDeclineSuccess() {
        when(meetingRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(object.meeting));

        object.meeting.saveInvite(1);

        String error = meetingService.activateInviteAndGetError(1, 1, false);

        Assert.assertNull(error);
    }

    @Test
    public void testCheckMeetingInviteActivateAcceptSuccess() {
        when(meetingRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(object.meeting));
        when(meetingRepository.findAllEmployeeAcceptedMeetings(1, 1)).thenReturn(new ArrayList<>());

        object.meeting.saveInvite(1);

        String error = meetingService.activateInviteAndGetError(1, 1, true);

        Assert.assertNull(error);
    }

    @Test
    public void testCheckMeetingInviteActivateAcceptError() {
        object.meeting2.setRoomId(2);
        object.meeting2.setStart(new Timestamp(150, 2, 1, 15, 0, 0, 0));
        object.meeting2.setEnd(new Timestamp(150, 2, 1, 16, 0, 0, 0));

        object.meetingList = new ArrayList<>();
        object.meetingList.add(object.meeting);

        object.meeting.saveInvite(1);
        object.meeting.activateInvite(1, true);

        object.meeting2.saveInvite(2);

        when(meetingRepository.findById(2)).thenReturn(java.util.Optional.ofNullable(object.meeting2));
        when(meetingRepository.findAllEmployeeAcceptedMeetings(1, 2)).thenReturn(object.meetingList);

        String error = meetingService.activateInviteAndGetError(1, 2, true);

        Assert.assertEquals("Employee meeting intersects with meetings: |Test|", error);

    }




}
