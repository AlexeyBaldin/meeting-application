package com.controller;

import com.model.employee.Employee;
import com.model.employee.Role;
import com.model.employee.User;
import com.model.employee.UserFactory;
import com.service.employee.EmployeeService;
import com.service.employee.RoleService;
import com.service.employee.UserService;
import com.service.office.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/rest/employee")
public class EmployeeRestController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    OfficeService officeService;


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerEmployee(@RequestBody Employee newEmployee) {

        Map<String, Object> responseMap = employeeService.checkEmployeeAndGetErrorsMap(newEmployee);

        if(responseMap.isEmpty()) {
            Employee employee = employeeService.saveEmployee(newEmployee);
            User user = userService.register(UserFactory.createUser(employee));

            responseMap.put("success", true);
            responseMap.put("login", user.getUsername());
            return ResponseEntity.ok().body(responseMap);
        } else {
            responseMap.put("success", false);
            return ResponseEntity.badRequest().body(responseMap);
        }
    }


    @PutMapping("/{employee_id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable(value = "employee_id") Integer employeeId,
                                                              @RequestBody Employee newEmployee) {

        if(employeeService.isEmployeeExist(employeeId) && officeService.isOfficeExists(newEmployee.getOfficeId())) {

            Map<String, Object> responseMap = employeeService.checkEmployeeAndGetErrorsMap(newEmployee);

            if(responseMap.isEmpty()) {
                employeeService.updateEmployee(employeeId, newEmployee);
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

    @DeleteMapping("/{employee_id}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable(value = "employee_id") Integer employeeId) {

        if(employeeService.isEmployeeExist(employeeId)) {
            employeeService.deleteEmployee(employeeId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);

            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
