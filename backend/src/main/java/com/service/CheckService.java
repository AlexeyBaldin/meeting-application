package com.service;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalTime;

@Service
public class CheckService {

    public String checkNullStringAndGetError(String string) {
        if(string == null) {
            return "is null";
        }
        return null;
    }

    public String checkNullLocalTimeAndGetError(LocalTime time) {
        if(time == null) {
            return "is null";
        }
        return null;
    }

    public String checkNullTimestampAndGetError(Timestamp date) {
        if(date == null) {
            return "is null";
        }
        return null;
    }

    public String checkPositiveIntegerAndGetError(Integer integer) {
        if(integer == 0) {
            return "is zero";
        } else if(integer < 0) {
            return "is negative";
        }
        return null;
    }

}
