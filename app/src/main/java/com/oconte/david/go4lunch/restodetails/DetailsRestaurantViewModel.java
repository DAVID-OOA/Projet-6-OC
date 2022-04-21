package com.oconte.david.go4lunch.restodetails;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.repositories.RestaurantDetailRepository;
import com.oconte.david.go4lunch.repositories.UserRepository;
import com.oconte.david.go4lunch.repositories.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsRestaurantViewModel extends ViewModel {

    Boolean isLiked;

    private RestaurantDetailRepository restaurantDetailRepository;

    // Repository
    private final UserRepository userRepository;

    // For Licked Restaurant
    private final MutableLiveData<Boolean> restaurantLikedMutableLiveData = new MutableLiveData<Boolean>();
    public LiveData<Boolean> getRestaurantsLikedLiveData() {
        return restaurantLikedMutableLiveData;
    }

    // For Picked Restaurant
    private final MutableLiveData<Boolean> restaurantPickedMutableLiveData = new MutableLiveData<Boolean>();
    public LiveData<Boolean> getRestaurantsPickedLiveData() {
        return restaurantPickedMutableLiveData;
    }

    private final MutableLiveData<List<User>> usersPicked = new MutableLiveData<>();
    public  LiveData<List<User>> getUsersPicked() {
        return usersPicked;
    }

    // For Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("restaurants");


    public DetailsRestaurantViewModel(RestaurantDetailRepository restaurantDetailRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.restaurantDetailRepository = restaurantDetailRepository;
    }

    // When click on liked button restaurant
    public void onLikedOnButtonClick(String idRestaurant) {
        if (!isLiked) {
            if (userRepository.isCurrentUserLogged()) {
                createRestaurant(idRestaurant);
            }
        } else {
            deleteRestaurant(idRestaurant);
        }
    }

    public void getDataRestaurantClick(String idRestaurant) {
        restaurantDetailRepository.getLikedUsersFromRestaurant(idRestaurant).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && idRestaurant != null){
                    FirebaseUser user = userRepository.getCurrentUser();
                    collectionReference.document(idRestaurant).collection("liked").document(Objects.requireNonNull(user).getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            if (Objects.requireNonNull(snapshot).exists()) {
                                isLiked = true;
                            } else {
                                isLiked = false;
                            }
                            restaurantLikedMutableLiveData.postValue(isLiked);
                        }
                    });
                }
            }
        });
    }

    // When click on picked button restaurant
    public void onPickedOnButtonClick(String idRestaurant, String uid, String nameRestaurantPicked, String adressRestaurantPicked, String photoUrlRestaurantpicked) {
            if (userRepository.isCurrentUserLogged()) {
                userRepository.getUserRestaurantPicked(uid).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null) {
                            User user = task.getResult().toObject(User.class);

                            if(Objects.requireNonNull(user).getIdRestaurantPicked().equals("")) {
                                userRepository.addRestaurantPicked(idRestaurant, nameRestaurantPicked, adressRestaurantPicked, photoUrlRestaurantpicked);
                                restaurantDetailRepository.createRestaurantDetailsPicked(idRestaurant);
                            } else if (user.getIdRestaurantPicked().equals(idRestaurant)) {
                                userRepository.deleteRestaurantPicked();
                                restaurantDetailRepository.deleteRestaurantDetailsUnPickedFromFirestore(idRestaurant);
                            } else if (!user.getIdRestaurantPicked().equals(idRestaurant)) {
                                userRepository.addRestaurantPicked(idRestaurant, nameRestaurantPicked, adressRestaurantPicked, photoUrlRestaurantpicked);
                                restaurantDetailRepository.deleteRestaurantDetailsUnPickedFromFirestore(user.getIdRestaurantPicked());
                                restaurantDetailRepository.createRestaurantDetailsPicked(idRestaurant);
                            }
                        }
                    }
                });
            }
    }

    public void getDataRestaurantPickedClick(String idRestaurant) {
        restaurantDetailRepository.getPickedUsersFromRestaurant(idRestaurant).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && idRestaurant != null){
                    FirebaseUser user = userRepository.getCurrentUser();
                    collectionReference.document(idRestaurant).collection("picked").document(Objects.requireNonNull(user).getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {

                            restaurantPickedMutableLiveData.postValue(Objects.requireNonNull(snapshot).exists());
                        }
                    });
                }
            }
        });
    }

    // create restaurant
    public void createRestaurant(String idRestaurant) {
        restaurantDetailRepository.createRestaurantDetailsLiked(idRestaurant);
    }

    // Delete restaurant
    public void deleteRestaurant(String idRestaurant) {
        restaurantDetailRepository.deleteRestaurantDetailsDislikedFromFirestore(idRestaurant);
    }

    // For the List of UserPicked this Restaurant
    public void getUserListPicked(String idRestaurant) {
        restaurantDetailRepository.getAllUserPickedFromFirebase(idRestaurant)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<User> userAdd = new ArrayList<>();

                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                User user = documentSnapshot.toObject(User.class);
                                if(Objects.requireNonNull(user).getUid() != null) {
                                    userAdd.add(user);
                                }
                            }
                            usersPicked.postValue(userAdd);
                        }
                    }
                });
    }
}
