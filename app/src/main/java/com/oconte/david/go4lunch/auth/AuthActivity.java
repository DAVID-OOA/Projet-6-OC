package com.oconte.david.go4lunch.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.oconte.david.go4lunch.MainActivity;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.SettingsActivity;
import com.oconte.david.go4lunch.databinding.ActivityAuthBinding;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity {

    //FOR DATA
    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    //FOR DESIGN
    @BindView(R.id.auth_activity_layout) CoordinatorLayout coordinatorLayout;
    //private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityAuthBinding.inflate(getLayoutInflater());
        //View view = binding.getRoot();
        //setContentView(view);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @OnClick(R.id.main_activity_button_login_google)
    public void onClickLoginButton() {
        // 3 - Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivity();
    }


   /* binding.main_activity_button_login_google.setOnClickListener(new View.OnClickListener() {
        viewModel.userClicked(),
        this.startSignInActivity();
    });*/


    // 2 - Launch Sign-In Activity
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

    //////////////////////////////////////////////////////
    // UI
    ///////////////////////////////////////////////////////

    // 2 - Show Snack Bar with a message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    ///////////////////////////////////////////////////////
    // UTILS
    //////////////////////////////////////////////////////
    // 3 - Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.startMainActivity();
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, "error_authentication_canceled");
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.coordinatorLayout, "error_no_internet");
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.coordinatorLayout, "error_unknown_error");
                }
            }
        }
    }

    // For started the MainActivity
    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
