package com.model.employee;

public class UserFactory {

    private UserFactory() {

    }


    private static String generateLogin(String name, int id) {
        return name.replaceAll(" ", "").toLowerCase() + id;
    }

    private static String generatePassword(String name) {
        return name.replaceAll(" ", "").toLowerCase();
    }

    public static User createUser(Employee employee) {
        User user = new User();

        user.setId(employee.getId());
        user.setUsername(generateLogin(employee.getName(), employee.getId()));
        user.setPassword(generatePassword(employee.getName()));

        return user;
    }
}
