package com.oconte.david.go4lunch;

import static org.junit.Assert.assertEquals;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.oconte.david.go4lunch.models.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class UserUnitTest {
    private User user;
    private String uid;
    private String username;
    private String email;
    private String urlPicture;
    private String idRestaurantPicked;

    @Before
    public void setup() {
        //C'est cette partie qui fait planter le test
        username = "Android Studio";
        uid = "01012022";
        email = "androidstudio@email.com";
        urlPicture = "http://photo";
        idRestaurantPicked = "123456987";
    }

    @Test
    public void getCorrectInfoFromUser() throws Exception {
        assertEquals(uid, user.getUid());
    }
}
