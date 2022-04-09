package com.oconte.david.go4lunch.listView;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.repositories.RestaurantDetailRepository;
import com.oconte.david.go4lunch.repositories.RestaurantRepository;
import com.oconte.david.go4lunch.repositories.UserRepository;
import com.oconte.david.go4lunch.util.ForPosition;


import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListRestaurantViewModel extends ViewModel {

    //Boolean isPicked;

    private final RestaurantDetailRepository restaurantDetailRepository;
    private final RestaurantRepository mRestaurantRepositoryImpl;
    private final UserRepository userRepository;
    private final MutableLiveData<List<Result>> apiNearByResponseMutableLiveData = new MutableLiveData<>();

    // For Picked Restaurant marker
    private final MutableLiveData<Boolean> restaurantMarkerPickedMutableLiveData = new MutableLiveData<Boolean>();
    public LiveData<Boolean> getRestaurantsMarkerPickedLiveData() {
        return restaurantMarkerPickedMutableLiveData;
    }

    // For info Of User connected by firebase
    private final MutableLiveData<User> userInfoConnected = new MutableLiveData<>();
    public LiveData<User> getUserInfoConnecteds() {
        return userInfoConnected;
    }

    // For Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("restaurants");

    public ListRestaurantViewModel(UserRepository userRepository, RestaurantDetailRepository restaurantDetailRepository) {
        mRestaurantRepositoryImpl =  Injection.getRestaurantNearBy(Injection.getService(), Injection.resource);
        this.userRepository = userRepository;
        this.restaurantDetailRepository = restaurantDetailRepository;
    }

    public LiveData<List<Result>> getRestaurantLiveData() {
            return apiNearByResponseMutableLiveData;
    }

    // contient l'info de la list des restaurants
    public void getRestaurants() {
        mRestaurantRepositoryImpl.getRestaurantNearBy(new RestaurantRepository.GetRestaurantsCallback() {
            @Override
            public void onResponse(@Nullable ApiNearByResponse apiNearByResponse) {
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (apiNearByResponse != null) {
                            List<Result> restaurants = calculateDistances(myLocation, apiNearByResponse.results);

                            for (Result restaurant : restaurants) {
                                restaurant.setNumberPeoplePicked(getNumberPeople(restaurant.getPlaceId()));
                            }
                            apiNearByResponseMutableLiveData.postValue(restaurants);
                        }
                    }
                });
            }
            @Override
            public void onFailure() {

            }
        }, ForPosition.convertLocationForApi(myLocation));
    }

    private int getNumberPeople(String placeId) {
        int i;
        Task<QuerySnapshot> querySnapshotTask = restaurantDetailRepository.getAllUserPickedFromFirebase(placeId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            }
        });
        try {
            QuerySnapshot await = Tasks.await(querySnapshotTask);
            i = await.size();
        } catch (ExecutionException e) {
            return 0;
        } catch (InterruptedException e) {
            return 0;
        }

        return i;
    }

    // contains the information of the selected restaurant
    private final MutableLiveData<Result> selectedRestaurant = new MutableLiveData<>();
    public void selectRestaurant(Result result) {
        selectedRestaurant.postValue(result);
    }

    public LiveData<Result> getSelectedRestaurant() {
        return selectedRestaurant;
    }

    // contains position information
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

    // Delete User
    public void deleteUser(String uid) {
        userRepository.deleteUserFromFirestore(uid);
    }

    // return if log or not
    public boolean isCurrentUserLogged() {
        return userRepository.isCurrentUserLogged();
    }

    public FirebaseUser isForGetCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public void updateUserName(String username) {
        userRepository.updateUsername(username);
    }

    public void getUserInfoConnected() {
        userRepository.getUserInfoConnected().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                userInfoConnected.postValue(user);

            }
        });
    }

}
