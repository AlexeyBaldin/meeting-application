package com.validation;

import com.util.FieldChecker;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalTime;

public class FieldCheckerValidationTest {

    @Test
    public void checkNullStringSuccess() {
        String error = FieldChecker.checkNullStringAndGetError("string");
        Assert.assertNull(error);
    }

    @Test
    public void checkNullStringError() {
        String error = FieldChecker.checkNullStringAndGetError(null);
        Assert.assertNotNull(error);
    }

    @Test
    public void checkNullLocalTimeSuccess() {
        String error = FieldChecker.checkNullLocalTimeAndGetError(LocalTime.now());
        Assert.assertNull(error);
    }

    @Test
    public void checkNullLocalTimeError() {
        String error = FieldChecker.checkNullLocalTimeAndGetError(null);
        Assert.assertNotNull(error);
    }

    @Test
    public void checkNullTimestampSuccess() {
        String error = FieldChecker.checkNullTimestampAndGetError(new Timestamp(7777));
        Assert.assertNull(error);
    }

    @Test
    public void checkNullTimestampError() {
        String error = FieldChecker.checkNullTimestampAndGetError(null);
        Assert.assertNotNull(error);
    }

    @Test
    public void checkNullPositiveIntegerSuccess() {
        String error = FieldChecker.checkPositiveIntegerAndGetError(7777);
        Assert.assertNull(error);
    }

    @Test
    public void checkNullPositiveIntegerError() {
        String error = FieldChecker.checkPositiveIntegerAndGetError(null);
        String error2 = FieldChecker.checkPositiveIntegerAndGetError(0);
        String error3 = FieldChecker.checkPositiveIntegerAndGetError(-5);
        Assert.assertNotNull(error);
        Assert.assertNotNull(error2);
        Assert.assertNotNull(error3);
    }
}
