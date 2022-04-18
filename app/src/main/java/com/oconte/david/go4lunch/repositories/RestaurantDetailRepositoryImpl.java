package com.oconte.david.go4lunch.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.models.User;

import java.util.Objects;

public class RestaurantDetailRepositoryImpl implements RestaurantDetailRepository {

    private static final String COLLECTION_NAME = "restaurants";
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebaseFirestore;

    public RestaurantDetailRepositoryImpl(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
    }

    //Get the Collection Reference
    private CollectionReference getRestaurantDetailsCollection(){
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    @Override
    public void createRestaurantDetailsLiked(String idRestaurant) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        firebaseFirestore.collection(UserRepository.COLLECTION_NAME).document(Objects.requireNonNull(currentUser).getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);

                if (currentUser.getUid().equals(Objects.requireNonNull(user).getUid())) {

                    String uid = user.getUid();
                    String username = user.getUsername();
                    String urlPhoto = Objects.requireNonNull(user.getUrlPicture());
                    String addressRestaurant = "";

                    Restaurant restaurant = new Restaurant(idRestaurant, username, uid, urlPhoto, addressRestaurant);

                    getRestaurantDetailsCollection().document(idRestaurant).collection("liked").document(uid).set(restaurant, SetOptions.merge());
                }
            }
        });
    }

    @Override
    public void deleteRestaurantDetailsDislikedFromFirestore(String idRestaurant) {

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (idRestaurant != null && currentUser != null) {
            String uid = currentUser.getUid();
            getRestaurantDetailsCollection().document(idRestaurant).collection("liked").document(uid).delete();
        }
    }

    @Override
    public Task<DocumentSnapshot> getLikedUsersFromRestaurant(String idRestaurant) {
        return getRestaurantDetailsCollection().document(idRestaurant).collection("liked").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).get();
    }

    @Override
    public void createRestaurantDetailsPicked(String idRestaurant) {

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        firebaseFirestore.collection(UserRepository.COLLECTION_NAME).document(Objects.requireNonNull(currentUser).getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);

                if (currentUser.getUid().equals(Objects.requireNonNull(user).getUid())) {

                    String uid = user.getUid();
                    String username = user.getUsername();
                    String urlPhoto = Objects.requireNonNull(user.getUrlPicture());
                    String addressRestaurant = "";

                    Restaurant restaurant = new Restaurant(idRestaurant, username, uid, urlPhoto, addressRestaurant);

                    getRestaurantDetailsCollection().document(idRestaurant).collection("picked").document(uid).set(restaurant, SetOptions.merge());
                }
            }
        });
    }

    @Override
    public void deleteRestaurantDetailsUnPickedFromFirestore(String idRestaurant) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (idRestaurant != null && currentUser != null) {
            String uid = currentUser.getUid();
            getRestaurantDetailsCollection().document(idRestaurant).collection("picked").document(uid).delete();
        }
    }

    @Override
    public Task<DocumentSnapshot> getPickedUsersFromRestaurant(String idRestaurant) {
        return getRestaurantDetailsCollection().document(idRestaurant).collection("picked").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).get();
    }

    @Override
    public Task<QuerySnapshot> getAllUserPickedFromFirebase(String idRestaurant) {
        return getRestaurantDetailsCollection().document(idRestaurant).collection("picked").orderBy("username").get();
    }

    @Override
    public void updateUsername(String username) {
        String uid = Objects.requireNonNull(this.firebaseAuth.getCurrentUser()).getUid();

        firebaseFirestore.collection(UserRepository.COLLECTION_NAME).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);

                String idRestaurantPicked = null;

                if (user != null) {
                    idRestaurantPicked = user.getIdRestaurantPicked();

                    getRestaurantDetailsCollection().document(idRestaurantPicked).collection("picked").document(uid).update("username", username);
                }
            }
        });
    }

    @Override
    public void updateUrlPicture(String urlPicture, String uid) {

        firebaseFirestore.collection(UserRepository.COLLECTION_NAME).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);

                String idRestaurant = null;

                if (user != null) {
                    idRestaurant = user.getIdRestaurantPicked();

                    getRestaurantDetailsCollection().document(idRestaurant).collection("picked").document(uid).update("urlPicture", urlPicture);
                }
            }
        });
    }

}
