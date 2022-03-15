package com.repository.employee;

import com.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "SELECT employee.* FROM employee JOIN invite USING(employee_id)" +
            " WHERE meeting_id = :meeting_id",
            nativeQuery = true)
    List<Employee> findAllEmployeeByMeetingId(@Param("meeting_id") Integer meetingId);

}
