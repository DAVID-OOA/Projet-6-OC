package com.oconte.david.go4lunch.restodetails;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.oconte.david.go4lunch.workMates.UserRepository;

import java.util.Objects;

public class DetailsRestaurantViewModel extends ViewModel {

    Boolean isLiked;

    private final UserRepository userRepository;

    private final RestaurantDetailRepository mRestaurantDetailRepository;

    private final MutableLiveData<Boolean> restaurantMutableLiveData = new MutableLiveData<Boolean>();

    public LiveData<Boolean> getRestaurantsLiveData() {
        return restaurantMutableLiveData;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("restaurants");

    public DetailsRestaurantViewModel(RestaurantDetailRepository restaurantDetailRepository,UserRepository userRepository) {
        this.mRestaurantDetailRepository = restaurantDetailRepository;
        this.userRepository = userRepository;
    }

    public void getDataRestaurantClick(String idRestaurant) {
        mRestaurantDetailRepository.getLikedUsersFromRestaurant(idRestaurant).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && idRestaurant != null){
                    FirebaseUser user = UserRepository.getInstance().getCurrentUser();
                    collectionReference.document(idRestaurant).collection("liked").document(Objects.requireNonNull(user).getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            //si document n'est pas vide le boutton prend la couleur jaune.
                            if (Objects.requireNonNull(snapshot).exists()) {
                                isLiked = true;
                            } else {
                                isLiked = false;
                            }
                            restaurantMutableLiveData.postValue(isLiked);
                        }
                    });
                }
            }
        });
    }

    // create restaurant
    public void createRestaurant(String idRestaurant) {
        mRestaurantDetailRepository.createRestaurantDetailsLiked(idRestaurant);
    }

    public void deleteRestaurant(String idRestaurant) {
        mRestaurantDetailRepository.deleteRestaurantDetailsDislikedFromFirestore(idRestaurant);
    }

    public void onLikedOnButtonClick(String idRestaurant) {
        if (!isLiked) {
            if (userRepository.isCurrentUserLogged()) {
                createRestaurant(idRestaurant);
            }
        } else {
            deleteRestaurant(idRestaurant);
        }
    }

    public void onPickedOnButtonClick(String idRestaurant) {

    }
}
