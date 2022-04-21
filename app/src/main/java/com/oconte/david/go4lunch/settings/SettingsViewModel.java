package com.oconte.david.go4lunch.settings;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oconte.david.go4lunch.repositories.RestaurantDetailRepository;
import com.oconte.david.go4lunch.repositories.UserRepository;

import java.util.Objects;
import java.util.UUID;

public class SettingsViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final RestaurantDetailRepository restaurantDetailRepository;

    private final FirebaseUser user;

    public String newPhotoUrl;

    public SettingsViewModel(UserRepository userRepository, RestaurantDetailRepository restaurantDetailRepository) {
        this.userRepository = userRepository;
        user = userRepository.getCurrentUser();
        this.restaurantDetailRepository = restaurantDetailRepository;
    }


    // For update User Name
    public void updateUserName(String username) {
        userRepository.updateUsername(username);
        restaurantDetailRepository.updateUsername(username);
    }

    // For update Email
    public void updateEmail(String email) {
        userRepository.updateEmail(email);
    }

    // For update a new user photo
    public void updatePhotoUser(String newPhotoUrl) {
        uploadPhotoInFirebase(newPhotoUrl);
    }

    private void uploadPhotoInFirebase(final String urlPhoto) {
        String uuid = UUID.randomUUID().toString();
        StorageReference imageRef = FirebaseStorage.getInstance().getReference(uuid);
        imageRef.putFile(Uri.parse(urlPhoto))
                .addOnSuccessListener(this::getUrlPhotoFromFirebase);
    }

    private void getUrlPhotoFromFirebase(UploadTask.TaskSnapshot taskSnapshot){
        Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    newPhotoUrl = uri.toString();
                    userRepository.updateUrlPicture(newPhotoUrl, user.getUid());
                });
    }

    // Delete User
    public void deleteUser(String uid) {
        userRepository.deleteUserFromFirestore(uid);
    }

}
