package com.oconte.david.go4lunch.utils;

import static com.oconte.david.go4lunch.util.ForPosition.convertLocationForApi;
import static org.junit.Assert.assertEquals;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ForPositionUnitTest {

    @Test
    public void convertLocationForApiIsCorrect() throws Exception{
        Double latitude = 44.5327;
        Double longitude = 0.7677;
        LatLng location = new LatLng(latitude,longitude);

        assertEquals(latitude.toString() + "," + longitude.toString(), convertLocationForApi(location));

    }

}
