package com.oconte.david.go4lunch.workMates;

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

public final class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static volatile UserRepository instance;

    private UserRepository(){
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


    // Delete the User from Firestore
    public void deleteUserFromFirestore(String uid) {
        if(uid != null){
            this.getUserCollection().document(uid).delete();
        }
    }

    // Get all Users
    public Task<QuerySnapshot> getAllUserFromFirebase() {
        return getUserCollection().orderBy("username").get();
    }
}
