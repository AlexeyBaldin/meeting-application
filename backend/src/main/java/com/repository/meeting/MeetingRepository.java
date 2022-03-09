package com.repository.meeting;

import com.dto.query.MeetingWithInvite;
import com.model.employee.Employee;
import com.model.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

    @Query(value = "SELECT meeting.* FROM meeting" +
            " WHERE room_id = :room_id ORDER BY meeting.meeting_start ASC",
            nativeQuery = true)
    List<Meeting> findAllByRoomId(@Param("room_id")Integer roomId);

    @Query(value = "SELECT DISTINCT meeting.* FROM meeting JOIN invite USING(meeting_id)" +
            " WHERE room_id = :room_id AND meeting_id != :meeting_id ORDER BY meeting.meeting_start ASC",
            nativeQuery = true)
    List<Meeting> findAllByRoomIdWithInvite(@Param("room_id")Integer roomId, @Param("meeting_id") Integer meetingId);

    @Query(value = "SELECT meeting.* FROM meeting JOIN invite USING(meeting_id)" +
            " WHERE employee_id = :employee_id AND meeting_id != :meeting_id AND invite_accept = 1",
            nativeQuery = true)
    List<Meeting> findAllEmployeeAcceptedMeetings(@Param("employee_id") Integer employeeId, @Param("meeting_id") Integer meetingId);

    @Query(value = "SELECT meeting.*, invite.invite_accept FROM meeting JOIN invite USING(meeting_id)" +
            " WHERE employee_id = :employee_id",
            nativeQuery = true)
    List<MeetingWithInvite> findAllEmployeeMeetingsWithInvite(@Param("employee_id") Integer employeeId);

}
