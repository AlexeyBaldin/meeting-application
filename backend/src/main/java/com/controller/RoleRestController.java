package com.controller;

import com.model.employee.Role;
import com.service.employee.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/role")
public class RoleRestController {

    @Autowired
    RoleService roleService;

    @GetMapping("/all")
    public List<Role> findAllRoles() {
        return roleService.findAllRoles();
    }

    @GetMapping("/{role_id}")
    public ResponseEntity<Role> findRoleById(@PathVariable(value = "role_id") Integer roleId) {
        if(roleService.isRoleExists(roleId)) {
            Role role = roleService.findRoleById(roleId);
            return ResponseEntity.ok().body(role);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveNewRole(@RequestBody Role newRole) {

        Map<String, Object> responseMap = roleService.checkRoleAndGetErrorsMap(newRole);

        if(responseMap.isEmpty()) {
            roleService.saveNewRole(newRole);
            responseMap.put("success", true);
            return ResponseEntity.ok().body(responseMap);
        } else {
            responseMap.put("success", false);
            return ResponseEntity.badRequest().body(responseMap);
        }

    }

    @PutMapping("/{role_id}")
    public ResponseEntity<Map<String, Object>> updateRole(@PathVariable(value = "role_id") Integer roleId, @RequestBody Role newRole) {

        if(roleService.isRoleExists(roleId)) {
            Map<String, Object> responseMap = roleService.checkRoleAndGetErrorsMap(newRole);

            if(responseMap.isEmpty()) {
                roleService.updateRole(roleId, newRole);
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

    @DeleteMapping("/{role_id}")
    public ResponseEntity<Map<String, Object>> deleteRole(@PathVariable(value = "role_id") Integer roleId) {

        if(roleService.isRoleExists(roleId)) {
            Map<String, Object> responseMap = new HashMap<>();

            roleService.deleteRole(roleId);
            responseMap.put("success", true);

            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

