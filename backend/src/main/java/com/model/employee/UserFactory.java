package com.model.employee;

import java.util.Objects;

public class UserFactory {

    private UserFactory() {

    }

    private static String hashPassword(String password) {
        int hash = 0;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            hash = ((hash << 5) - hash) + c;
        }
        return Integer.toString(hash);
    }

    private static String generateLogin(String name, int id) {
        return name.replaceAll(" ", "").toLowerCase() + id;
    }

    private static String generatePassword(String password) {
        return hashPassword(password.replaceAll(" ", "").toLowerCase());
    }

    public static User createUser(Employee employee) {
        User user = new User();

        user.setId(employee.getId());
        user.setUsername(generateLogin(employee.getName(), employee.getId()));
        user.setPassword(generatePassword(employee.getName()));

        return user;
    }
}
