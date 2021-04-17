package com.oconte.david.go4lunch.listView;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.oconte.david.go4lunch.api.GooglePlaceService;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.Result;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    private final GooglePlaceService service;
    private final CountingIdlingResource resource;

    private MutableLiveData<List<Result>> apiNearByResponseMutableLiveData;

    public MutableLiveData<List<Result>> getResults() {
        return apiNearByResponseMutableLiveData;
    }

    /**
     * It's the Call to API GooglePlace.
     */

    // Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable ApiNearByResponse apiNearByResponse);
        void onFailure();
    }

    public RestaurantRepository(GooglePlaceService service, CountingIdlingResource resource) {
        this.service = service;
        this.resource = resource;
    }

    // Public methode to start fetching
    public void getRestaurantNearBy(RestaurantRepository.Callbacks callbacks, String location) {

        resource.increment();
        // weak reference to callback (avoid memory leaks)
        //final WeakReference<RestaurantRepository.Callbacks> callbacksWeakReference = new WeakReference<RestaurantRepository.Callbacks>(callbacks);

        // The call on NYT API
        Call<ApiNearByResponse> call = service.getRestaurantNearBy(location);

        // Start the Call
        call.enqueue(new Callback<ApiNearByResponse>() {
            @Override
            public void onResponse(Call<ApiNearByResponse> call, Response<ApiNearByResponse> apiNearByResponseResponse) {

                // Call the proper callback used in controller mainfragment
                //if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(apiNearByResponseResponse.body());
                callbacks.onResponse(apiNearByResponseResponse.body());
                resource.decrement();
            }

            @Override
            public void onFailure(Call<ApiNearByResponse> call, Throwable t) {

                // Call the proper callback used in controller mainfragment
                //if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
                callbacks.onFailure();
                resource.decrement();
            }
        });

    }
}
