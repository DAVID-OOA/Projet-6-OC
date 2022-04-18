package com.oconte.david.go4lunch.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface RestaurantDetailRepository {
        void createRestaurantDetailsLiked(String idRestaurant);
        void deleteRestaurantDetailsDislikedFromFirestore(String idRestaurant);
        Task<DocumentSnapshot> getLikedUsersFromRestaurant(String idRestaurant);
        void createRestaurantDetailsPicked(String idRestaurant);
        void deleteRestaurantDetailsUnPickedFromFirestore(String idRestaurant);
        Task<DocumentSnapshot> getPickedUsersFromRestaurant(String idRestaurant);
        Task<QuerySnapshot> getAllUserPickedFromFirebase(String idRestaurant);
        void updateUsername(String username);
        void updateUrlPicture(String urlPicture, String uid);
    }