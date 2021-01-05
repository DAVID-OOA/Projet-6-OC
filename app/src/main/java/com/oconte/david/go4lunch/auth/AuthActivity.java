package com.oconte.david.go4lunch.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.oconte.david.go4lunch.MainActivity;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.SettingsActivity;
import com.oconte.david.go4lunch.databinding.ActivityAuthBinding;

import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity {

    //FOR DATA
    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    // Identify each Http Request
    private static final int SIGN_OUT_TASK = 10;

    //FOR DESIGN;
    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.onClickLoginButton();

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    //For Signing
    ///////////////////////////////////////////////////////////////////////////////////////////////
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

    public void onClickLoginButton(){
        binding.mainActivityButtonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch Sign-In Activity when user clicked on Login Button
                startSignInActivity();
            }
        });
    }

    // Launch Sign-In Activity
    private void startSignInActivity(){
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build())
                                        //new AuthUI.IdpConfig.FacebookBuilder().build())
                        )
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.go4lunch_icon)
                        .build(), RC_SIGN_IN);
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
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



}
