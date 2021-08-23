package com.oconte.david.go4lunch.util;

import com.google.android.gms.maps.model.LatLng;

public class ForPosition {

    public static String convertLocationForApi(LatLng position){
        if(position != null) {
            Double lat = position.latitude;
            Double lng = position.longitude;

            return lat.toString() + "," + lng.toString();
        }
        return null;
    }


}
