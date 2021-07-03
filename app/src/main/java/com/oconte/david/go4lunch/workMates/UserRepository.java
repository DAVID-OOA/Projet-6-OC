package com.oconte.david.go4lunch.workMates;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.models.User;

public final class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private CollectionReference userCollection;
    private User user;

    private static volatile UserRepository INSTANCE;

    public static UserRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UserRepository();
        }
        return INSTANCE;
    }

    private UserRepository(){
        this.userCollection = getUserCollection();
    }

    private CollectionReference getUserCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //---- CREATE ----
    public Task<Void> createUser(String uid, String username, String email, String urlPicture){
        user = new User(uid, username, email, urlPicture);
        return userCollection.document(uid).set(user);
    }

    //---- GET ----
    public User getUser(){
        return user;
    }

    public Task<DocumentSnapshot> getUserFromFirebase(String uid){
        return userCollection.document(uid).get();

    }

    //---- UPDATE ----
    public Task<Void> updateUserNameAndEmail(String username, String email, String uid){
        user.setUsername(username);
        user.setEmail(email);
        return userCollection.document(uid).update(
                "username", username, "email", email);
    }

    public Task<Void> updateUrlPicture(String urlPicture, String uid){
        user.setUrlPicture(urlPicture);
        return userCollection.document(uid).update("urlPicture", urlPicture);
    }

}
