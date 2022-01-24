package com.controller;

import com.model.employee.User;
import com.service.employee.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/user")
public class UserRestController {

    @Autowired
    UserService userService;

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

    @PostMapping("/save")
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
    }

    @PutMapping("/{user_id}")
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
}
