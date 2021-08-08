package com.oconte.david.go4lunch.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.ActivityAuthBinding;
import com.oconte.david.go4lunch.workMates.UserRepository;

import java.util.Arrays;
import java.util.Objects;


public class AuthActivity extends AppCompatActivity {

    //FOR DATA
    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    // Identify each Http Request
    //private static final int SIGN_OUT_TASK = 10;

    //FOR DESIGN;
    private ActivityAuthBinding binding;

    public static final String EXTRA_IS_CONNECTED = "extra_is_connected";

    // For firebase
    @SuppressLint("StaticFieldLeak")
    private static volatile AuthActivity instance;
    private final UserRepository userRepository;

    public AuthActivity() {
        userRepository = UserRepository.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.startSignInActivity();

    }


    // For Signing
    public void setUpSignActivity(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Launch Sign-In Activity
    private void startSignInActivity(){
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build())
                        )
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.go4lunch_icon)
                        .build(), RC_SIGN_IN);
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK ) { // SUCCESS
                this.setIsConnected();
                this.createUser();
                this.setUpSignActivity();
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(binding.authActivityLayout, "error_authentication_canceled");
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(binding.authActivityLayout, "error_no_internet");
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(binding.authActivityLayout, "error_unknown_error");
                }
            }
        }
    }

    //////////////////////////////////////////////////////
    // UI
    ///////////////////////////////////////////////////////

    // Show Snack Bar with a message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    /////////////////////////////////////////////////////
    //For Info about connected user

    public void createUser(){
        userRepository.createUser();
    }

    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public static AuthActivity getInstance() {
        AuthActivity result = instance;
        if(result != null){
            return result;
        }
        synchronized (AuthActivity.class) {
            if (instance == null) {
                instance = new AuthActivity();
            }
            return instance;
        }
    }

    private void setIsConnected() {
        SharedPreferences preferences = getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(EXTRA_IS_CONNECTED, true);
        editor.apply();
    }
}
