package com.oconte.david.go4lunch.settings;

import static com.oconte.david.go4lunch.util.SuccessOrigin.UPDATE_PHOTO;
import static com.oconte.david.go4lunch.util.TextUtil.isEmailCorrect;
import static com.oconte.david.go4lunch.util.TextUtil.isTextLongEnough;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.repositories.UserRepository;
import com.oconte.david.go4lunch.util.SuccessOrigin;

import java.util.Objects;
import java.util.UUID;

public class SettingsViewModel extends ViewModel {

    // PUBLIC LIVE DATA
    public MutableLiveData<String> urlPicture = new MutableLiveData<>();
    public MutableLiveData<Boolean> isEmailError = new MutableLiveData<>();
    public MutableLiveData<Integer> errorMessageEmail = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUsernameError = new MutableLiveData<>();
    public MutableLiveData<Integer> errorMessageUsername = new MutableLiveData<>();

    private final UserRepository userRepository;
    //private final RestaurantFirebaseRepository restaurantFirebaseRepository;

    private FirebaseUser user;

    private String newPhotoUrl;

    public SettingsViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.restaurantFirebaseRepository = restaurantFirebaseRepository;
        user = userRepository.getCurrentUser();
    }

    public void updateUserName(String username) {
        userRepository.updateUsername(username);
    }

    public void updateUserPhoto(String toString) {

    }

    // --------------------
    // SET NEW PICTURE
    // --------------------

    private void uploadPhotoInFirebase(final String urlPhoto) {
        String uuid = UUID.randomUUID().toString();
        StorageReference imageRef = FirebaseStorage.getInstance().getReference(uuid);
        imageRef.putFile(Uri.parse(urlPhoto))
                .addOnSuccessListener(this::getUrlPhotoFromFirebase);
                //.addOnFailureListener(this.onFailureListener(UPDATE_PHOTO_DB));

    }

    private void getUrlPhotoFromFirebase(UploadTask.TaskSnapshot taskSnapshot){
        Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    newPhotoUrl = uri.toString();
                    userRepository.updateUrlPicture(newPhotoUrl, String.valueOf(userRepository.getCurrentUser()))
                            .addOnSuccessListener(onSuccessListener(UPDATE_PHOTO));
                            //.addOnFailureListener(this.onFailureListener(UPDATE_PHOTO_DB));
                });//.addOnFailureListener(this.onFailureListener(UPDATE_PHOTO_DB));

    }

    // --------------------
    // UTILS
    // --------------------

    private boolean isNewUserInfosCorrect(String email, String username){
        if(!isEmailCorrect(email)){
            isEmailError.setValue(true);
            errorMessageEmail.setValue(R.string.incorrect_email);
            return false;
        }
        if(!isTextLongEnough(username, 3)){
            isUsernameError.setValue(true);
            errorMessageUsername.setValue(R.string.incorrect_username);
            return false;
        }

        return true;

    }

    private OnSuccessListener<Void> onSuccessListener(final SuccessOrigin origin){
        return aVoid -> {
            switch (origin){
                case UPDATE_USER:
                    //snackBarText.setValue(new Event<>(R.string.information_updated));
                    //isLoading.setValue(false);
                    //user.setEmail(newEmail);
                    //user.setUsername(newUsername);
                    break;
                case DELETE_USER:
                    //snackBarText.setValue(new Event<>(R.string.deleted_account_message));
                    //deleteUser.setValue(new Event<>(new Object()));
                    //isLoading.setValue(false);
                    break;
                case UPDATE_PHOTO:
                    //snackBarText.setValue(new Event<>(R.string.photo_updated_message));
                    //isLoading.setValue(false);
                    urlPicture.setValue(newPhotoUrl);
                    //user.setUrlPicture(newPhotoUrl);
                    break;
            }

        };

    }

    // Delete User
    public void deleteUser(String uid) {
        userRepository.deleteUserFromFirestore(uid);
    }

}
