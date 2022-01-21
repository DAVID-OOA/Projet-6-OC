package com.oconte.david.go4lunch.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.ActivitySettingsBinding;
import com.oconte.david.go4lunch.restodetails.ViewModelFactory;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    //For Design
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;

    //String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.configureViewDetailsRestaurantFactory(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance());

        this.configureToolbar();

        String username = String.valueOf(binding.usernameField.getText());

        this.updateInfoOfUser(username);

    }

    public void configureViewDetailsRestaurantFactory(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(firebaseAuth,firebaseFirestore);
        ViewModelProvider viewModelProvider = new ViewModelProvider(SettingsActivity.this, viewModelFactory);
        viewModel = viewModelProvider.get(SettingsViewModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *  Configure the Toolbar
     */
    protected void configureToolbar() {
        setSupportActionBar(binding.layoutToolbar.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("I'm Hungry !");
    }

    // FOR UPDATE INFO USER
    public void updateInfoOfUser(String username) {
        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.updateUserName(username);
            }
        });
    }

    // FOR DELETE ACCOUNT
    //@OnClick(R.id.profile_activity_button_delete)
    public void onClickDeleteButton() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(R.string.popup_message_choice_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //String uid = Objects.requireNonNull(userRepository.getCurrentUser()).getUid();
                        //userRepository.deleteUserFromFirestore(uid);
                    }
                })
                .setNegativeButton(R.string.popup_message_choice_no, null)
                .show();
    }

    // FOR ADD PHOTO
}
