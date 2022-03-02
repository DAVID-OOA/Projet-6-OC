package com.oconte.david.go4lunch.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oconte.david.go4lunch.auth.AuthViewModel;
import com.oconte.david.go4lunch.listView.ListRestaurantViewModel;
import com.oconte.david.go4lunch.restodetails.DetailsRestaurantViewModel;
import com.oconte.david.go4lunch.settings.SettingsViewModel;
import com.oconte.david.go4lunch.workMates.WorkMatesViewModel;

import org.jetbrains.annotations.NotNull;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RestaurantDetailRepository restaurantDetailRepository;
    private final UserRepository userRepository;
    //private final RestaurantFirebaseRepository restaurantFirebaseRepository;

    public ViewModelFactory(RestaurantDetailRepository restaurantDetailRepository, UserRepository userRepository) {
        this.restaurantDetailRepository = restaurantDetailRepository;
        this.userRepository = userRepository;
        //this.restaurantFirebaseRepository = restaurantFirebaseRepository;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailsRestaurantViewModel.class)) {
            return (T) new DetailsRestaurantViewModel(restaurantDetailRepository, userRepository);
        }
        if (modelClass.isAssignableFrom(ListRestaurantViewModel.class)) {
            return (T) new ListRestaurantViewModel(userRepository, restaurantDetailRepository);
        }
        if (modelClass.isAssignableFrom(WorkMatesViewModel.class)) {
            return (T) new WorkMatesViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(userRepository);
        }
        throw new IllegalArgumentException("ViewModel Not Found");
    }
}
