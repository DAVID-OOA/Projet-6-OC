package com.oconte.david.go4lunch.restodetails;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.models.User;

import java.util.List;

public class RestaurantDetailRepository {

    private static final String COLLECTION_NAME = "restaurants";

    private static volatile RestaurantDetailRepository instance;

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

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    //Get the Collection Reference
    private CollectionReference getRestaurantDetailsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /*Create RestaurantDetails
    public void createRestaurantDetail() {
        FirebaseUser currentRestaurantDetail = getCurrentUser();
        if (currentRestaurantDetail != null) {
            String idRestaurant = currentRestaurantDetail.getProviderId();
            List<String> likedRestaurant = currentRestaurantDetail.zzg();
            String uid = currentRestaurantDetail.getUid();


            Restaurant restaurantRepositoryCreate = new Restaurant(idRestaurant, likedRestaurant);
            this.getRestaurantDetailsCollection().document(idRestaurant).collection(uid).document().set(restaurantRepositoryCreate);

        }
    }*/
}
