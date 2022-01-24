package com.controller;

import com.model.employee.Employee;
import com.model.employee.Role;
import com.service.employee.EmployeeService;
import com.service.employee.RoleService;
import com.service.office.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/employee")
public class EmployeeRestController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    RoleService roleService;

    @Autowired
    OfficeService officeService;

    @GetMapping("/all")
    public List<Employee> findAll() {
        return employeeService.findAllEmployees();
    }

    @GetMapping("/{employee_id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable(value = "employee_id") Integer employeeId) {
        if(employeeService.isEmployeeExist(employeeId)) {
            Employee employee = employeeService.findEmployeeById(employeeId);
            return ResponseEntity.ok().body(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{employee_id}/roles")
    public ResponseEntity<List<Role>> findEmployeeRoleById(@PathVariable(value = "employee_id") Integer employeeId) {
        if(employeeService.isEmployeeExist(employeeId)) {
            List<Role> roles = employeeService.findEmployeeRoleByEmployeeId(employeeId);
            return ResponseEntity.ok().body(roles);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveEmployee(@RequestBody Employee newEmployee) {

        Map<String, Object> responseMap = employeeService.checkEmployeeAndGetErrorsMap(newEmployee);

        if(responseMap.isEmpty()) {
            employeeService.saveEmployee(newEmployee);
            responseMap.put("success", true);
            return ResponseEntity.ok().body(responseMap);
        } else {
            responseMap.put("success", false);
            return ResponseEntity.badRequest().body(responseMap);
        }

    }

    @PostMapping("/{employee_id}/role/{role_id}")
    public ResponseEntity<Map<String, Object>> saveEmployeeNewRole(@PathVariable(value = "employee_id") Integer employeeId,
                                                                   @PathVariable(value = "role_id") Integer roleId) {
        if(employeeService.isEmployeeExist(employeeId) && roleService.isRoleExists(roleId)) {

            Map<String, Object> responseMap = employeeService.checkEmployeeRoleAndGetErrorsMap(true, employeeId, roleId);

            if(responseMap.isEmpty()) {
                employeeService.saveNewEmployeeRole(employeeId, roleId);
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

    @DeleteMapping("/{employee_id}/role/{role_id}")
    public ResponseEntity<Map<String, Object>> deleteEmployeeRole(@PathVariable(value = "employee_id") Integer employeeId,
                                                                  @PathVariable(value = "role_id") Integer roleId) {
        if(employeeService.isEmployeeExist(employeeId) && roleService.isRoleExists(roleId)) {

            Map<String, Object> responseMap = employeeService.checkEmployeeRoleAndGetErrorsMap(false, employeeId, roleId);

            if(responseMap.isEmpty()) {
                employeeService.deleteEmployeeRole(employeeId, roleId);
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
