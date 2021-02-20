package com.oconte.david.go4lunch;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    public static String BASE_URL = "https://maps.googleapis.com/maps/api";
    public static String API_KEY = "AIzaSyBq5YQ-3LwqD0gaRZ9B7zJf5tYw2qhSQHo";

    @GET("/place/details/json?fields=place_id&key=" + API_KEY)
    Call<ApiDetailResponse> getRestaurantDetail(@Query("placeid") String placeId);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build();
}
