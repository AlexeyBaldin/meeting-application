package com.service.employee;

import com.model.employee.User;
import com.repository.employee.EmployeeRepository;
import com.repository.employee.UserRepository;
import com.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    CheckService checkService;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void saveUser(User newUser) {
        userRepository.save(newUser);
    }

    public void updateUser(Integer userId, User newUser) {
        User user = findUserById(userId);

        newUser.setId(userId);
        newUser.setActivation(user.isActivation());

        userRepository.save(newUser);
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

        String check = checkUserLoginAndGetError(newUser.getLogin());
        if(check != null) {
            errors.put("field(login) error", check);
        }

        return errors;
    }

    private String checkUserLoginAndGetError(String userLogin) {
        String error = checkService.checkNullStringAndGetError(userLogin);

        if(error == null && userRepository.existsByLogin(userLogin)) {
            error = "user with login = " + userLogin + " already exist";
        }

        return error;
    }

    public void activateUser(Integer userId) {
        User user = findUserById(userId);
        user.setActivation(true);
        userRepository.save(user);
    }
}

