package com.oconte.david.go4lunch.listView;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.util.ForPosition;
import com.oconte.david.go4lunch.workMates.UserRepository;

import java.util.List;

public class ListRestaurantViewModel extends ViewModel {

    private final RestaurantRepository mRestaurantRepository;
    private final MutableLiveData<List<Result>> apiNearByResponseMutableLiveData = new MutableLiveData<>();

    UserRepository userRepository;
    User user;

    public ListRestaurantViewModel() {
        mRestaurantRepository =  Injection.getRestaurantNearBy(Injection.getService(), Injection.resource);
    }

    public LiveData<List<Result>> getRestaurantLiveData() {
            return apiNearByResponseMutableLiveData;
    }

    // contient l'info de la list des restaurants
    public void getRestaurants() {
        //Classe Anonyme
        mRestaurantRepository.getRestaurantNearBy(new RestaurantRepository.Callbacks() {
            @Override
            public void onResponse(@Nullable ApiNearByResponse apiNearByResponse) {
                if (apiNearByResponse != null) {
                    List<Result> restaurants = calculateDistances(myLocation, apiNearByResponse.results);
                    apiNearByResponseMutableLiveData.postValue(restaurants);
                }
            }

            @Override
            public void onFailure() {

            }
        }, ForPosition.convertLocationForApi(myLocation));
    }

    // contient l'information du restaurant selectionné
    private final MutableLiveData<Result> selectedRestaurant = new MutableLiveData<>();
    public void selectRestaurant(Result result) {
        //mettre a jour l'info
        selectedRestaurant.postValue(result);
    }

    public LiveData<Result> getSelectedRestaurant() {
        //recuperer l'information pour l'utiliser
        return selectedRestaurant;
    }

    // contient l'information de la position
    private LatLng myLocation = null;
    public void setMyLocation(LatLng myLocation) {
        this.myLocation = myLocation;
    }

    public LatLng getMyLocation() {
        return myLocation;
    }

    // Calculate the distance for listRestaurant.
    private List<Result> calculateDistances(LatLng myLocation, List<Result> results) {
        for (Result result: results) {
            distanceBetweenPositionAndResto(result,myLocation);
        }
        return results;
    }

    @SuppressLint("DefaultLocale")
    private void distanceBetweenPositionAndResto(Result result, LatLng myLocation){
        if (myLocation != null && result != null) {
            Double latitude = result.getGeometry().getLocation().getLat();
            Double longitude = result.getGeometry().getLocation().getLng();
            LatLng positionRestaurant = new LatLng(latitude, longitude);

            Double distance = SphericalUtil.computeDistanceBetween(myLocation, positionRestaurant);
            result.getGeometry().setDistance(distance);
        }
    }

}
