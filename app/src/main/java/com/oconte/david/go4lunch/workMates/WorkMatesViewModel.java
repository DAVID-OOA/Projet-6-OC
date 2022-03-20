package com.oconte.david.go4lunch.workMates;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.repositories.UserRepository;
import com.oconte.david.go4lunch.repositories.UserRepositoryImpl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkMatesViewModel extends ViewModel {

    private final UserRepository mUserRepository;

    // PRIVATE LIVE DATTA
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();


    public WorkMatesViewModel(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    // GETTER LIVE DATTA
    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void getUserList() {
        // TODO Check de connection internet
        mUserRepository.getAllUserFromFirebase()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<User> fetcheUsers = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                User userFetched = documentSnapshot.toObject(User.class);
                                if (Objects.requireNonNull(userFetched).getUid() != null) {
                                    fetcheUsers.add(userFetched);
                                }
                            }
                            users.postValue(fetcheUsers);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.w("TAG", "Error load document", e);
                    }
                });
    }
}
