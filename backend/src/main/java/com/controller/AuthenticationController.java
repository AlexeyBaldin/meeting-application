package com.controller;

import com.dto.AuthenticationRequestDto;
import com.model.employee.User;
import com.security.jwt.JwtTokenProvider;
import com.service.employee.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/rest/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        try {
            String username = authenticationRequestDto.getUsername();
            System.out.println(username);
            System.out.println(authenticationRequestDto.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    authenticationRequestDto.getPassword()));


            User user = userService.findByUsername(username);

            if(user == null) {
                throw new UsernameNotFoundException("User with username + " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            response.put("admin", user.isAdmin());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            System.out.println("Invalid username or password");
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
