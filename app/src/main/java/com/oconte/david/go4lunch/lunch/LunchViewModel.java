package com.oconte.david.go4lunch.lunch;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.repositories.UserRepository;

import java.util.Objects;

public class LunchViewModel extends ViewModel {

    private final MutableLiveData<Restaurant> lunchRestaurantPicked = new MutableLiveData<>();
    public LiveData<Restaurant> getLunchRestaurantPickedData() {
        return  lunchRestaurantPicked;
    }

    private final UserRepository userRepository;

    public LunchViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void getUserRestaurantPicked(String uid) {
        if (userRepository.isCurrentUserLogged()) {

            userRepository.getUserRestaurantPicked(uid).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult() != null) {
                        User user = task.getResult().toObject(User.class);

                        String idRestaurantPicked = Objects.requireNonNull(user).getIdRestaurantPicked();
                        String photoUrlRestaurant = user.getPhotoUrlRestaurantpicked();
                        String nameRestaurantPicked = user.getNameRestaurantPicked();
                        String uid = user.getUid();
                        String addressRestaurant = user.getAdressRestaurantPicked();

                        Restaurant lunchRestaurant = new Restaurant(idRestaurantPicked, nameRestaurantPicked, uid, photoUrlRestaurant, addressRestaurant);

                        lunchRestaurantPicked.postValue(lunchRestaurant);
                    }
                }
            });
        }
    }
}
