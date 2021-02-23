package com.oconte.david.go4lunch;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    public static String API_KEY = "AIzaSyBq5YQ-3LwqD0gaRZ9B7zJf5tYw2qhSQHo";



    //@GET("place/details/json?fields=vicinity,place_id,id,geometry,opening_hours,name,rating,photo&key=" + API_KEY)
    //Call<ApiDetailResponse> getRestaurantDetail(@Query("placeid") String placeId);

    @GET("place/nearbysearch/json?type=restaurant&radius=3000&key=" + API_KEY)
    Call<ApiDetailResponse> getRestaurantDetail(@Query("location") String location);


}
