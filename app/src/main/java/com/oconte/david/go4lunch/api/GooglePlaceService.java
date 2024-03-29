package com.oconte.david.go4lunch.api;

import com.oconte.david.go4lunch.BuildConfig;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.ApiRestaurantDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    String myApiKey = BuildConfig.MAPS_API_KEY;

    @GET("place/nearbysearch/json?radius=3000&type=restaurant&key=" + myApiKey)
    Call<ApiNearByResponse> getRestaurantNearBy(@Query("location") String location);

}
