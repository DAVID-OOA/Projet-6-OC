package com.oconte.david.go4lunch.api;

import com.oconte.david.go4lunch.BuildConfig;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.ApiRestaurantDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    public static String API_KEY = "AIzaSyBq5YQ-3LwqD0gaRZ9B7zJf5tYw2qhSQHo";



    @GET("place/details/json?fields=vicinity,place_id,id,geometry,opening_hours,name,rating,photo&key=" + API_KEY)
    Call<ApiRestaurantDetails> getRestaurantDetail(@Query("placeid") String placeId);

    @GET("place/nearbysearch/json?location=44.532616, 0.768252&radius=3000&type=restaurant&key=" + API_KEY)
    Call<ApiNearByResponse> getRestaurantNearBy(@Query("location") String location);


}
