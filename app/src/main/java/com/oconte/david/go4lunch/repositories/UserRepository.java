package com.oconte.david.go4lunch.repositories;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oconte.david.go4lunch.models.User;

public interface UserRepository {

    User getUser();
    @Nullable
    FirebaseUser getCurrentUser();
    Boolean isCurrentUserLogged();
    void createUser();
    void deleteUserFromFirestore(String uid);
    Task<Void> updateUsername(String username);
    Task<Void> updateUrlPicture(String urlPicture, String uid);
    Task<QuerySnapshot> getAllUserFromFirebase();
    void addRestaurantPicked(String idRestaurant, String nameRestaurantPicked, String adressRestaurantPicked, String photoUrlRestaurantpicked);
    void deleteRestaurantPicked();
    Task<DocumentSnapshot> getUserRestaurantPicked(String uid);
    Task<Void> updateEmail(String email);
    Task<DocumentSnapshot> getInfoUserConnected();
}
