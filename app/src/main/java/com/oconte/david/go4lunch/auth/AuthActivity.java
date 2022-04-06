package com.oconte.david.go4lunch.auth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.MainActivity;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.ActivityAuthBinding;
import com.oconte.david.go4lunch.repositories.ViewModelFactory;

import java.util.Arrays;


public class AuthActivity extends AppCompatActivity {

    //FOR DESIGN;
    private ActivityAuthBinding binding;

    public static final String EXTRA_IS_CONNECTED = "extra_is_connected";

    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.startSignInActivity();

        this.configureViewDetailsRestaurantFactory();
    }

    public void configureViewDetailsRestaurantFactory() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(AuthActivity.this, viewModelFactory);
        viewModel = viewModelProvider.get(AuthViewModel.class);
    }

    // For Signing
    public void setUpSignActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    setIsConnected();
                    createUser();
                    setUpSignActivity();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Your auth is not worked, try again");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
    });

    private void startSignInActivity() {
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(
                        Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build(),
                                new AuthUI.IdpConfig.TwitterBuilder().build())
                )
                .setIsSmartLockEnabled(false, true)
                .setLogo(R.drawable.go4lunch_icon)
                .build();

        signInLauncher.launch(signInIntent);
    }

    //For Info about connected user
    public void createUser(){
        viewModel.createUser();
    }

    private void setIsConnected() {
        SharedPreferences preferences = getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(EXTRA_IS_CONNECTED, true);
        editor.apply();
    }

}
