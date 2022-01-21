package com.oconte.david.go4lunch.models;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UserUnitTest {
    private User user;
    private String uid;
    private String username;
    private String email;
    private String urlPicture;
    private String idRestaurantPicked;

    @Before
    public void setup() {
        username = "Android Studio";
        uid = "01012022";
        email = "androidstudio@email.com";
        urlPicture = "http://photo";
        idRestaurantPicked = "123456987";

        user = new User(uid,username,email,urlPicture,idRestaurantPicked);
    }

    @Test
    public void getCorrectInfoFromUser() throws Exception {
        assertEquals(uid, user.getUid());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(urlPicture, user.getUrlPicture());
    }
}
