package com.service.employee;

import com.model.employee.Role;
import com.model.employee.User;
import com.model.employee.UserFactory;
import com.repository.employee.EmployeeRepository;
import com.repository.employee.UserRepository;
import com.util.FieldChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void saveUser(User newUser) {
        userRepository.save(newUser);
    }

    public User register(User user) {

        Role roleUser = roleService.findRoleByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

        saveUser(user);

        return user;
    }

    public void updateUser(Integer userId, User newUser) {
        User user = findUserById(userId);

        if(newUser.getUsername() != null) {
            user.setUsername(newUser.getUsername());
        }


        if(newUser.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        }

        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        User user = findUserById(userId);

        userRepository.delete(user);
    }

    public boolean isUserExists(Integer userId) {
        return userRepository.existsById(userId);
    }

    public Map<String, Object> checkUserAndGetErrorsMap(User newUser) {
        Map<String, Object> errors = new HashMap<>();

        if(!employeeRepository.existsById(newUser.getId())) {
            errors.put("field(userId) error", "employee does`t exist for id = " + newUser.getId());
        }

        String check = checkUserUsernameAndGetError(newUser);
        if(check != null) {
            errors.put("field(username) error", check);
        }

        check = checkUserPasswordAndGetError(newUser.getPassword());
        if(check != null) {
            errors.put("field(password) error", check);
        }

        return errors;
    }

    private String checkUserUsernameAndGetError(User newUser) {
        String error = null;

        User user = userRepository.findByUsername(newUser.getUsername()).orElse(null);

        if(user != null && user.getId() != newUser.getId()) {
            error = "user with username = " + newUser.getUsername() + " already exist";
        }

        return error;
    }

    private String checkUserPasswordAndGetError(String password) {
        String error = null;

        if(password.length() < 5) {
            error = "password should be 5 or more symbols";
        }

        return error;
    }

    public void activateUser(Integer userId) {
        User user = findUserById(userId);
        user.setActivation(true);
        userRepository.save(user);
    }

    public List<Role> findEmployeeRoleByEmployeeId(Integer employeeId) {
        User user = findUserById(employeeId);
        return user.getRoles();
    }

    public void saveNewEmployeeRole(Integer employeeId, Integer roleId) {
        User user = findUserById(employeeId);
        Role role = roleService.findRoleById(roleId);

        user.addRole(role);
        saveUser(user);
    }

    public void deleteEmployeeRole(Integer employeeId, Integer roleId) {
        User user = findUserById(employeeId);
        Role role = roleService.findRoleById(roleId);

        user.deleteRole(role);
        saveUser(user);
    }

    public Map<String, Object> checkEmployeeRoleAndGetErrorsMap(Boolean exist, Integer employeeId, Integer roleId) {
        Map<String, Object> errors = new HashMap<>();
        User user = findUserById(employeeId);
        Role role = roleService.findRoleById(roleId);

        if(user.checkRole(role) && exist) {
            errors.put("employee role error", "employee with id = " + employeeId + " already in role with id = " + roleId);
        }

        if(!user.checkRole(role) && !exist) {
            errors.put("employee role error", "employee with id = " + employeeId + " not in role with id = " + roleId);
        }

        return errors;
    }
}

