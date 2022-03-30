package com.oconte.david.go4lunch.settings;

import static com.oconte.david.go4lunch.util.SuccessOrigin.UPDATE_PHOTO;
import static com.oconte.david.go4lunch.util.TextUtil.isEmailCorrect;
import static com.oconte.david.go4lunch.util.TextUtil.isTextLongEnough;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    private FirebaseUser user;

    private String newPhotoUrl;


    public SettingsViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        user = userRepository.getCurrentUser();
    }

    public void updateUserName(String username) {
        userRepository.updateUsername(username);
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
                    //isLoading.setValue(false);
                    //user.setEmail(newEmail);
                    //user.setUsername(newUsername);
                    break;
                case DELETE_USER:
                    //deleteUser.setValue(new Event<>(new Object()));
                    //isLoading.setValue(false);
                    break;
                case UPDATE_PHOTO:
                    updatePhotoUser();
                    break;
            }

        };

    }

    public void updatePhotoUser() {
        userRepository.updateUrlPicture(newPhotoUrl, user.getUid());
    }

    // Delete User
    public void deleteUser(String uid) {
        userRepository.deleteUserFromFirestore(uid);
    }

}
