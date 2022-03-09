package com.controller;

import com.model.employee.Role;
import com.model.employee.User;
import com.service.employee.RoleService;
import com.service.employee.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/rest/user")
public class UserRestController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @GetMapping("/all")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<User> findUserById(@PathVariable(value = "user_id") Integer userId) {
        if(userService.isUserExists(userId)) {
            User user = userService.findUserById(userId);
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*@PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveUser(@RequestBody User newUser) {

        Map<String, Object> responseMap = userService.checkUserAndGetErrorsMap(newUser);

        if(responseMap.isEmpty()) {
            userService.saveUser(newUser);
            responseMap.put("success", true);
            return ResponseEntity.ok().body(responseMap);
        } else {
            responseMap.put("success", false);
            return ResponseEntity.badRequest().body(responseMap);
        }
    }*/



    @PutMapping("/{user_id}/activation")
    public ResponseEntity<Map<String, Object>> activateUser(@PathVariable(value = "user_id") Integer userId) {

        if(userService.isUserExists(userId)) {
            userService.activateUser(userId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);

            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable(value = "user_id") Integer userId) {
        if(userService.isUserExists(userId)) {
            userService.deleteUser(userId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);

            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{employee_id}/roles")
    public ResponseEntity<List<Role>> findEmployeeRoleById(@PathVariable(value = "employee_id") Integer employeeId) {
        if(userService.isUserExists(employeeId)) {
            List<Role> roles = userService.findEmployeeRoleByEmployeeId(employeeId);
            return ResponseEntity.ok().body(roles);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{employee_id}/role/{role_id}")
    public ResponseEntity<Map<String, Object>> saveEmployeeNewRole(@PathVariable(value = "employee_id") Integer employeeId,
                                                                   @PathVariable(value = "role_id") Integer roleId) {
        if(userService.isUserExists(employeeId) && roleService.isRoleExists(roleId)) {

            Map<String, Object> responseMap = userService.checkEmployeeRoleAndGetErrorsMap(true, employeeId, roleId);

            if(responseMap.isEmpty()) {
                userService.saveNewEmployeeRole(employeeId, roleId);
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

    @DeleteMapping("/{employee_id}/role/{role_id}")
    public ResponseEntity<Map<String, Object>> deleteEmployeeRole(@PathVariable(value = "employee_id") Integer employeeId,
                                                                  @PathVariable(value = "role_id") Integer roleId) {
        if(userService.isUserExists(employeeId) && roleService.isRoleExists(roleId)) {

            Map<String, Object> responseMap = userService.checkEmployeeRoleAndGetErrorsMap(false, employeeId, roleId);

            if(responseMap.isEmpty()) {
                userService.deleteEmployeeRole(employeeId, roleId);
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
