package com.zaolt.civil;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MainTest {

    Main main = new Main();

    @Test
    public void getuserWhithinLondonTEST() {
        String url = "https://bpdts-test-app.herokuapp.com/users";
        List<Object> userList = main.getData(url);
        assertEquals(1000,userList.size());

    }

    @Test
    public void getUsersInsideDistanceTEST() {
        City city = new City();
        city.setName("London");
        city.setLatitude(51 + (30 / 60.0) + (26 / 60.0 / 60.0));
        city.setLongitude(0 - (7 / 60.0) - (39 / 60.0 / 60.0));
        int distanceInMile = 50;
        List<User> userList = main.getUsersInsideDistance(
                                  main.parseUserObject(
                                       main.getData("https://bpdts-test-app.herokuapp.com/users")),city, distanceInMile);
        assertEquals(3,userList.size());
    }

    @Test
    public void parseUserObjectTEST() {
        String url = "https://bpdts-test-app.herokuapp.com/city/London/users";
        List<User> userList = main.parseUserObject(main.getData(url));
        assertEquals(6,userList.size());
        url = "https://bpdts-test-app.herokuapp.com/city/Liverpool/users";
        userList = main.parseUserObject(main.getData(url));
        assertNotEquals(6,userList.size());

    }
}