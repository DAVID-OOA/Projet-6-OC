package com.oconte.david.go4lunch.repositories;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.oconte.david.go4lunch.models.User;

import java.util.Objects;

public class UserRepositoryImpl implements UserRepository{

    private static final String COLLECTION_NAME = "users";

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebaseFirestore;

    private User user;

    public UserRepositoryImpl(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore){
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Nullable
    @Override
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    @Override
    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    //Get the Collection Reference
    private CollectionReference getUserCollection(){
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    @Override
    public void createUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String uid = currentUser.getUid();
            String photoUrl = Objects.requireNonNull(currentUser.getPhotoUrl()).toString();
            String displayName = currentUser.getDisplayName();
            String idRestaurantPicked = "";
            String nameRestaurantPicked = "";
            String adressRestaurantPicked = "";
            String photoUrlRestaurantpicked = "";

            User userRepositoryCreate = new User(uid,displayName,email,photoUrl,idRestaurantPicked, nameRestaurantPicked, adressRestaurantPicked, photoUrlRestaurantpicked);
            this.getUserCollection().document(uid).set(userRepositoryCreate);
        }
    }

    @Override
    public void deleteUserFromFirestore(String uid) {
        if(uid != null){
            this.getUserCollection().document(uid).delete();
        }
    }

    @Override
    public Task<Void> updateUsername(String username) {
        String uid = Objects.requireNonNull(this.getCurrentUser()).getUid();
        if(uid != null){
            return this.getUserCollection().document(uid).update("username", username);
        }else{
            return null;
        }
    }

    @Override
    public Task<Void> updateUrlPicture(String urlPicture, String uid) {
        user.setUrlPicture(urlPicture);
        return getUserCollection().document(uid).update("urlPicture", urlPicture);
    }

    @Override
    public Task<QuerySnapshot> getAllUserFromFirebase() {
        return getUserCollection().orderBy("username").get();
    }

    @Override
    public void addRestaurantPicked(String idRestaurant, String nameRestaurantPicked, String adressRestaurantPicked, String photoUrlRestaurantpicked) {
        this.getUserCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("idRestaurantPicked", idRestaurant,
                "nameRestaurantPicked", nameRestaurantPicked,
                "adressRestaurantPicked", adressRestaurantPicked,
                "photoUrlRestaurantpicked", photoUrlRestaurantpicked);
    }

    @Override
    public void deleteRestaurantPicked() {
        this.getUserCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("idRestaurantPicked", "",
                "nameRestaurantPicked", "",
                "adressRestaurantPicked", "",
                "photoUrlRestaurantpicked", "");
    }

    @Override
    public Task<DocumentSnapshot> getUserRestaurantPicked(String uid) {
        return getUserCollection().document(uid).get();
    }

    /*---- GET ----
    public User getUser(){
        return user;
    }

    @Nullable
    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    //Get the Collection Reference
    private CollectionReference getUserCollection(){
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    //Create User
    public void createUser(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String uid = currentUser.getUid();
            String photoUrl = Objects.requireNonNull(currentUser.getPhotoUrl()).toString();
            String displayName = currentUser.getDisplayName();
            String idRestaurantPicked = "";
            String nameRestaurantPicked = "";
            String adressRestaurantPicked = "";
            String photoUrlRestaurantpicked = "";

            User userRepositoryCreate = new User(uid,displayName,email,photoUrl,idRestaurantPicked, nameRestaurantPicked, adressRestaurantPicked, photoUrlRestaurantpicked);
            this.getUserCollection().document(uid).set(userRepositoryCreate);
        }
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore(String uid) {
        if(uid != null){
            this.getUserCollection().document(uid).delete();
        }
    }

    // Update User Username
    public Task<Void> updateUsername(String username) {
        String uid = Objects.requireNonNull(this.getCurrentUser()).getUid();
        if(uid != null){
            return this.getUserCollection().document(uid).update("username", username);
        }else{
            return null;
        }
    }

    // Update UrlPhoto
    public Task<Void> updateUrlPicture(String urlPicture, String uid){
        user.setUrlPicture(urlPicture);
        return getUserCollection().document(uid).update("urlPicture", urlPicture);
    }

    // Get all Users
    public Task<QuerySnapshot> getAllUserFromFirebase() {
        return getUserCollection().orderBy("username").get();
    }

    public void addRestaurantPicked(String idRestaurant, String nameRestaurantPicked, String adressRestaurantPicked, String photoUrlRestaurantpicked) {
        this.getUserCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("idRestaurantPicked", idRestaurant, "nameRestaurantPicked", nameRestaurantPicked, "adressRestaurantPicked", adressRestaurantPicked, "photoUrlRestaurantpicked", photoUrlRestaurantpicked);
    }

    public void deleteRestaurantPicked() {
        this.getUserCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("idRestaurantPicked", "", "nameRestaurantPicked", "", "adressRestaurantPicked", "", "photoUrlRestaurantpicked", "");
    }

    // Get User restaurant picked
    public Task<DocumentSnapshot> getUserRestaurantPicked(String uid) {
        return getUserCollection().document(uid).get();
    }*/
}