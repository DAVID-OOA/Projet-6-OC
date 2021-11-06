package com.oconte.david.go4lunch.restodetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.workMates.UserRepository;

import java.util.Objects;

public class DetailsRestaurantViewModel extends ViewModel {



    private final RestaurantDetailRepository restaurantDetailRepository;

    private final MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();

    public LiveData<Restaurant> getRestaurantsMutableLiveData() {
        return restaurantMutableLiveData;
    }


    public DetailsRestaurantViewModel(UserRepository userRepository, RestaurantDetailRepository restaurantDetailRepository) {
        this.restaurantDetailRepository = restaurantDetailRepository;

    public void getDataRestaurantClick(String idRestaurant) {
        mRestaurantDetailRepository.getLikedUsersFromRestaurant(idRestaurant).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && idRestaurant != null){
                    FirebaseUser user = UserRepository.getInstance().getCurrentUser();
                    collectionReference.document(idRestaurant).collection("liked").document(Objects.requireNonNull(user).getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            //si document n'est pas vide le boutton prend la couleur jaune.
                            if (Objects.requireNonNull(snapshot).exists()) {
                                isLiked = true;

                                Log.d("TAG","switch on yellow !!!!!!!!!!!!!!!!!!");


                            } else {
                                isLiked = false;
                                Log.d("TAG","switch on black!!!!!!!!!!!!!!!!!!");
                            }
                            restaurantMutableLiveData.postValue(isLiked);
                        }
                    });
                }
            }
        });
    }

    // create restaurant
    public void createRestaurant(String idRestaurant) {
        mRestaurantDetailRepository.createRestaurantDetailsLiked(idRestaurant);
    }

    public void deleteRestaurant(String idRestaurant) {
        mRestaurantDetailRepository.deleteRestaurantDetailsDislikedFromFirestore(idRestaurant);
    }
}
