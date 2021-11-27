package com.oconte.david.go4lunch.listView;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;
import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.restodetails.RestaurantDetailRepository;
import com.oconte.david.go4lunch.util.ForPosition;
import com.oconte.david.go4lunch.workMates.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListRestaurantViewModel extends ViewModel {

    Boolean isPicked;

    private final RestaurantDetailRepository restaurantDetailRepository;
    private final RestaurantRepository mRestaurantRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<List<Result>> apiNearByResponseMutableLiveData = new MutableLiveData<>();

    // For Picked Restaurant marker
    private final MutableLiveData<Boolean> restaurantMarkerPickedMutableLiveData = new MutableLiveData<Boolean>();
    public LiveData<Boolean> getRestaurantsMarkerPickedLiveData() {
        return restaurantMarkerPickedMutableLiveData;
    }

    // For Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("restaurants");

    public ListRestaurantViewModel(UserRepository userRepository, RestaurantDetailRepository restaurantDetailRepository) {
        mRestaurantRepository =  Injection.getRestaurantNearBy(Injection.getService(), Injection.resource);
        this.userRepository = userRepository;
        this.restaurantDetailRepository = restaurantDetailRepository;
    }

    public LiveData<List<Result>> getRestaurantLiveData() {
            return apiNearByResponseMutableLiveData;
    }

    // contient l'info de la list des restaurants
    public void getRestaurants() {
        mRestaurantRepository.getRestaurantNearBy(new RestaurantRepository.Callbacks() {
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

    /*
    public void getDataRestaurantMarkerPicked(String idRestaurant) {
        restaurantDetailRepository.getPickedUsersFromRestaurant(idRestaurant).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && idRestaurant != null){
                    FirebaseUser user = userRepository.getCurrentUser();
                    collectionReference.document(idRestaurant).collection("picked").document(Objects.requireNonNull(user).getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            //si document n'est pas vide le boutton prend la couleur jaune.
                            if (Objects.requireNonNull(snapshot).exists()) {
                                isPicked = true;
                            } else {
                                isPicked = false;
                            }
                            restaurantMarkerPickedMutableLiveData.postValue(isPicked);
                        }
                    });
                }
            }
        });
    }*/

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

}
