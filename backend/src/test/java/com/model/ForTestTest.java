package com.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ForTestTest {

    private ForTest forTest;

    @Before
    public void setUp() {
        forTest = new ForTest(777, "Mr. Smith", 77);
    }

    @Test
    public void getId() {
        Assert.assertEquals(777, forTest.getId());
    }

    @Test
    public void getName() {
        Assert.assertEquals("Mr. Smith", forTest.getName());
    }

    @Test
    public void getAge() {
        Assert.assertEquals(77, forTest.getAge());
    }

    @Test
    public void setId() {
        forTest.setId(7777);
        Assert.assertEquals(7777, forTest.getId());
    }

    @Test
    public void setName() {
        forTest.setName("New Mr. Smith");
        Assert.assertEquals("New Mr. Smith", forTest.getName());
    }

    @Test
    public void setAge() {
        forTest.setAge(78);
        Assert.assertEquals(78, forTest.getAge());
    }

    @Test
    public void mockitoTest() {
        ForTest mockitoObj = Mockito.mock(ForTest.class);

        Mockito.when(mockitoObj.getId()).thenReturn(0);
        Assert.assertEquals(0, mockitoObj.getId());
    }
}