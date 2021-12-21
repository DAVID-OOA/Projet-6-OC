package com.oconte.david.go4lunch.settings;

import androidx.lifecycle.ViewModel;

import com.oconte.david.go4lunch.workMates.UserRepository;

public class SettingsViewModel extends ViewModel {

    private final UserRepository userRepository;

    public SettingsViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateUserName(String username) {
        userRepository.updateUsername(username);
    }
}
