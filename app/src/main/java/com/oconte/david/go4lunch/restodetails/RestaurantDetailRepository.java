package com.oconte.david.go4lunch.restodetails;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.oconte.david.go4lunch.models.Restaurant;

import java.util.Objects;

public class RestaurantDetailRepository {

    private static final String COLLECTION_NAME = "restaurants";
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebaseFirestore;

    public RestaurantDetailRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
    }

    //Get the Collection Reference
    private CollectionReference getRestaurantDetailsCollection(){
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    /*Create RestaurantDetails when is liked*/
    public void createRestaurantDetailsLiked(String idRestaurant) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {

            String uid = currentUser.getUid();
            String userName = currentUser.getDisplayName();
            String urlPhoto = Objects.requireNonNull(currentUser.getPhotoUrl()).toString();

            Restaurant restaurant = new Restaurant(idRestaurant, userName, uid, urlPhoto);

            getRestaurantDetailsCollection().document(idRestaurant).collection("liked").document(uid).set(restaurant, SetOptions.merge());
        }
    }

    /*Delete RestaurantDetails when is disliked*/
    public void deleteRestaurantDetailsDislikedFromFirestore(String idRestaurant) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (idRestaurant != null && currentUser != null) {
            String uid = currentUser.getUid();
            getRestaurantDetailsCollection().document(idRestaurant).collection("liked").document(uid).delete();
        }
    }

    // Get if someone liked this restaurant
    public Task<DocumentSnapshot> getLikedUsersFromRestaurant(String idRestaurant) {
        return getRestaurantDetailsCollection().document(idRestaurant).collection("liked").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).get();
    }

    //Create RestaurantDetails when is picked
    public void createRestaurantDetailsPicked(String idRestaurant) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {

            String uid = currentUser.getUid();
            String username = currentUser.getDisplayName();
            String urlPhoto = Objects.requireNonNull(currentUser.getPhotoUrl()).toString();

            Restaurant restaurant = new Restaurant(idRestaurant, username, uid, urlPhoto);

            getRestaurantDetailsCollection().document(idRestaurant).collection("picked").document(uid).set(restaurant, SetOptions.merge());
        }
    }

    //Delete RestaurantDetails when is unpicked
    public void deleteRestaurantDetailsUnPickedFromFirestore(String idRestaurant) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (idRestaurant != null && currentUser != null) {
            String uid = currentUser.getUid();
            getRestaurantDetailsCollection().document(idRestaurant).collection("picked").document(uid).delete();
        }
    }

    // Get if someone picked this restaurant
    public Task<DocumentSnapshot> getPickedUsersFromRestaurant(String idRestaurant) {
        return getRestaurantDetailsCollection().document(idRestaurant).collection("picked").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).get();
    }

    // Get all Users Picked
    public Task<QuerySnapshot> getAllUserPickedFromFirebase(String idRestaurant) {
        return getRestaurantDetailsCollection().document(idRestaurant).collection("picked").orderBy("username").get();
    }

}
