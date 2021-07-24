package com.oconte.david.go4lunch.workMates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.oconte.david.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;

public class WorkMatesViewModel extends ViewModel {


    private final UserRepository userRepository;
    private final MutableLiveData<List<User>> listUserMutableLiveData = new MutableLiveData<>();


    public WorkMatesViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<List<User>> getUserLiveData() {
        return listUserMutableLiveData;
    }

    public void getUserList() {
        userRepository.getAllUserFromFirebase()
                .addOnCompleteListener(queryDocumentSnapShots -> {
                    List<User> users = new ArrayList<>();


                });
    }
}
