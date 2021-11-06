package com.oconte.david.go4lunch.restodetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oconte.david.go4lunch.workMates.UserRepository;

import org.jetbrains.annotations.NotNull;

public class ViewModelDetailsRestaurantFactory implements ViewModelProvider.Factory {

    private final RestaurantDetailRepository restaurantDetailRepository;
    private final UserRepository userRepository;

    public ViewModelDetailsRestaurantFactory(RestaurantDetailRepository restaurantDetailRepository, UserRepository userRepository) {
        this.restaurantDetailRepository = restaurantDetailRepository;
        this.userRepository = userRepository;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailsRestaurantViewModel.class)) {
            return (T) new DetailsRestaurantViewModel(restaurantDetailRepository, userRepository);
        }
        throw new IllegalArgumentException("ViewModel Not Found");
    }
}
