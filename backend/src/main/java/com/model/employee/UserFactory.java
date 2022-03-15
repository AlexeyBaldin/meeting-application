package com.model.employee;


public class UserFactory {

    private UserFactory() {

    }

    private static String encryptPassword(String password) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < password.length(); i++) {
            int c = password.charAt(i) + i;
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    private static String generateLogin(String name, int id) {
        return name.replaceAll(" ", "").toLowerCase() + id;
    }

    private static String generatePassword(String password) {
        return encryptPassword(password.replaceAll(" ", "").toLowerCase());
    }

    public static User createUser(Employee employee) {
        User user = new User();

        user.setId(employee.getId());
        user.setUsername(generateLogin(employee.getName(), employee.getId()));
        user.setPassword(generatePassword(employee.getName()));

        return user;
    }
}
