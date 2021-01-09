package com.oconte.david.go4lunch.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oconte.david.go4lunch.MainActivity;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.SettingsActivity;
import com.oconte.david.go4lunch.databinding.ActivityAuthBinding;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AuthActivity extends AppCompatActivity {

    //FOR DATA
    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    // Identify each Http Request
    private static final int SIGN_OUT_TASK = 10;

    //FOR DESIGN;
    private ActivityAuthBinding binding;
    @BindView(R.id.imageview_header_navigationview) ImageView imageViewProfile;

    private static final String EXTRA_LOG = "extra_log";

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
            if (resultCode == RESULT_OK ) { // SUCCESS
                this.setUpSignActivity();
                getResultLogForTest();

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

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    // Update UI when activity is creating
    private void updateUIWhenCreating(){

        if (this.getCurrentUser() != null){

            //Get picture URL from Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Picasso.get()
                        .load(this.getCurrentUser().getPhotoUrl())
                        .transform(new CropCircleTransformation())
                        .into(this.imageViewProfile);
            }

            //Get email & username from Firebase
            //String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            //String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();

            //Update views with data
            //this.textInputEditTextUsername.setText(username);
            //this.textViewEmail.setText(email);
        }
    }

    private void getResultLogForTest() {


        String resultLog = String.valueOf(true);

        SharedPreferences preferences = getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EXTRA_LOG, resultLog);

        editor.apply();
    }


}
