package com.oconte.david.go4lunch.workMates;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.oconte.david.go4lunch.models.User;

import java.util.List;
import java.util.Objects;

public final class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static volatile UserRepository instance;
    private User user;

    public UserRepository(){
    }

    public static UserRepository getInstance(){
        UserRepository result = instance;
        if(result != null){
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
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
    private CollectionReference getUserCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //Create User
    public void createUser(){
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String uid = currentUser.getUid();
            String photoUrl = Objects.requireNonNull(currentUser.getPhotoUrl()).toString();
            String displayName = currentUser.getDisplayName();

            User userRepositoryCreate = new User(uid,displayName,email,photoUrl);
            this.getUserCollection().document(uid).set(userRepositoryCreate);

        }
    }

    // add Like Restaurant
    public Task<Void> addLikedRestaurant(String likedRestaurant, String uid) {
        user.addLikedRestaurant(likedRestaurant);
        return updateLikedRestaurant(uid);
    }

    public Task<Void> removeLikedRestaurant(String likedRestaurant, String uid){
        user.removeLikedRestaurant(likedRestaurant);
        return updateLikedRestaurant(uid);
    }


    private Task<Void> updateLikedRestaurant(String uid) {
        List<String> likedRestaurantsList = user.getLikedRestaurants();
        return getUserCollection().document(uid).update("likedRestaurants", likedRestaurantsList);
    }

    public User getUser() {
        return user;
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore(String uid) {
        if(uid != null){
            this.getUserCollection().document(uid).delete();
        }
    }

    // Update User Username
    public Task<Void> updateUsername(String username) {
        /*String uid = this.getCurrentUser();
        if(uid != null){
            return this.getUserCollection().document(uid).update(USERNAME_FIELD, username);
        }else*/{
            return null;
        }
    }

    // Get all Users
    public Task<QuerySnapshot> getAllUserFromFirebase() {
        return getUserCollection().orderBy("username").get();
    }
}
