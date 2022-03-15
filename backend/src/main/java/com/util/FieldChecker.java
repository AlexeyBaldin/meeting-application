package com.util;

import java.sql.Timestamp;
import java.time.LocalTime;


public class FieldChecker {

    private FieldChecker() {}

    public static String checkNullStringAndGetError(String string) {
        if(string == null) {
            return "is null";
        }
        return null;
    }

    public static String checkNullLocalTimeAndGetError(LocalTime time) {
        if(time == null) {
            return "is null";
        }
        return null;
    }

    public static String checkNullTimestampAndGetError(Timestamp date) {
        if(date == null) {
            return "is null";
        }
        return null;
    }

    public static String checkPositiveIntegerAndGetError(Integer integer) {
        if(integer == null) {
            return "is null";
        } else if(integer == 0) {
            return "is zero";
        } else if(integer < 0) {
            return "is negative";
        }
        return null;
    }
}
