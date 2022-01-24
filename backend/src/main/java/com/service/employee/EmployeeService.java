package com.service.employee;

import com.model.employee.Employee;
import com.model.employee.Role;
import com.repository.employee.EmployeeRepository;
import com.service.CheckService;
import com.service.office.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    OfficeService officeService;

    @Autowired
    CheckService checkService;

    public Boolean isEmployeeExist(Integer employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee findEmployeeById(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    public void saveEmployee(Employee newEmployee) {
        employeeRepository.save(newEmployee);
    }

    public void updateEmployee(Integer employeeId, Employee newEmployee) {
        Employee employee = findEmployeeById(employeeId);

        newEmployee.setId(employeeId);
        newEmployee.setEmployeeRole(employee.getEmployeeRole());
        newEmployee.setInvites(employee.getInvites());

        saveEmployee(newEmployee);
    }

    public void deleteEmployee(Integer employeeId) {
        Employee employee = findEmployeeById(employeeId);

        employeeRepository.delete(employee);
    }

    public List<Role> findEmployeeRoleByEmployeeId(Integer employeeId) {
        Employee employee = findEmployeeById(employeeId);
        return employee.getEmployeeRole();
    }

    public void saveNewEmployeeRole(Integer employeeId, Integer roleId) {
        Employee employee = findEmployeeById(employeeId);
        Role role = roleService.findRoleById(roleId);

        employee.addRole(role);
        saveEmployee(employee);
    }

    public void deleteEmployeeRole(Integer employeeId, Integer roleId) {
        Employee employee = findEmployeeById(employeeId);
        Role role = roleService.findRoleById(roleId);

        employee.deleteRole(role);
        saveEmployee(employee);
    }

    private String checkEmployeeNameAndGetError(String employeeName) {
        return checkService.checkNullStringAndGetError(employeeName);
    }

    private String checkEmployeeEmailAndGetError(String employeeEmail) {
        return checkService.checkNullStringAndGetError(employeeEmail);
    }

    private String checkEmployeePositionAndGetError(String employeePosition) {
        return checkService.checkNullStringAndGetError(employeePosition);
    }

    public Map<String, Object> checkEmployeeAndGetErrorsMap(Employee newEmployee) {
        Map<String, Object> errors = new HashMap<>();

        if(!officeService.isOfficeExists(newEmployee.getOfficeId())) {
            errors.put("field(officeId) error", "office does`t exist for id = " + newEmployee.getOfficeId());
        }

        String check = checkEmployeeNameAndGetError(newEmployee.getName());
        if(check != null) {
            errors.put("field(name) error", check);
        }

        check = checkEmployeeEmailAndGetError(newEmployee.getEmail());
        if(check != null) {
            errors.put("field(email) error", check);
        }

        check = checkEmployeePositionAndGetError(newEmployee.getPosition());
        if(check != null) {
            errors.put("field(position) error", check);
        }


        return errors;
    }

    public Map<String, Object> checkEmployeeRoleAndGetErrorsMap(Boolean exist, Integer employeeId, Integer roleId) {
        Map<String, Object> errors = new HashMap<>();
        Employee employee = findEmployeeById(employeeId);
        Role role = roleService.findRoleById(roleId);

        if(employee.checkRole(role) && exist) {
            errors.put("employee role error", "employee with id = " + employeeId + " already in role with id = " + roleId);
        }

        if(!employee.checkRole(role) && !exist) {
            errors.put("employee role error", "employee with id = " + employeeId + " not in role with id = " + roleId);
        }

        return errors;
    }
}
