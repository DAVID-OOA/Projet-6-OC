package com.oconte.david.go4lunch.restodetails;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.workMates.UserRepository;

import java.util.List;
import java.util.Objects;

public class RestaurantDetailRepository {

    private static final String COLLECTION_NAME = "restaurants";

    private static volatile RestaurantDetailRepository instance;

    String idUser;

    public RestaurantDetailRepository() {
    }

    public static RestaurantDetailRepository getInstance() {
        RestaurantDetailRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized ( RestaurantDetailRepository.class) {
            if (instance == null) {
                instance = new RestaurantDetailRepository();
            }
            return instance;
        }
    }

    @Nullable
    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    //Get the Collection Reference
    private CollectionReference getRestaurantDetailsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /*Create RestaurantDetails when is liked*/
    public void createRestaurantDetail(String idRestaurant) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {

            String idUser = currentUser.getUid();
            String userName = currentUser.getDisplayName();
            String urlPhoto = Objects.requireNonNull(currentUser.getPhotoUrl()).toString();

            Restaurant restaurant = new Restaurant(urlPhoto,userName,idUser, idRestaurant);

            getRestaurantDetailsCollection().document(idRestaurant).collection("liked").document(idUser).set(restaurant, SetOptions.merge());
        }
    }

    /*Delete RestaurantDetails when is disliked*/
    public void deleteRestaurantDetailsDislikedFromFirestore(String idRestaurant) {
        FirebaseUser currentUser = getCurrentUser();
        if (idRestaurant != null && currentUser != null) {
            String idUser = currentUser.getUid();
            getRestaurantDetailsCollection().document(idRestaurant).collection("liked").document(idUser).delete();
        }

    }
}
