package com.oconte.david.go4lunch.auth;

import android.annotation.SuppressLint;
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

import com.firebase.ui.auth.AuthUI;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.ActivityAuthBinding;
import com.oconte.david.go4lunch.workMates.UserRepository;

import java.util.Arrays;


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
                                new AuthUI.IdpConfig.FacebookBuilder().build())
                )
                .setIsSmartLockEnabled(false, true)
                .setLogo(R.drawable.go4lunch_icon)
                .build();

        signInLauncher.launch(signInIntent);
    }

    //////////////////////////////////////////////////////
    // UI
    ///////////////////////////////////////////////////////

    /* Show Snack Bar with a message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }*/

    /////////////////////////////////////////////////////
    //For Info about connected user

    public void createUser(){
        userRepository.createUser();
    }

    private void setIsConnected() {
        SharedPreferences preferences = getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(EXTRA_IS_CONNECTED, true);
        editor.apply();
    }

}
