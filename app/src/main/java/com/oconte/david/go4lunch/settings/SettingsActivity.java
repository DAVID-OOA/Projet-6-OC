package com.oconte.david.go4lunch.settings;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.ActivitySettingsBinding;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.repositories.ViewModelFactory;

import java.util.Objects;

import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingsActivity extends AppCompatActivity {

    //For Design
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;

    //String username;
    // DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

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

        this.addPhotoUser();

    }

    public void configureViewDetailsRestaurantFactory(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(firebaseAuth,firebaseFirestore);
        ViewModelProvider viewModelProvider = new ViewModelProvider(SettingsActivity.this, viewModelFactory);
        viewModel = viewModelProvider.get(SettingsViewModel.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
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
    @OnClick(R.id.delete_button)
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

    public void addPhotoUser() {
        binding.photoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromPhone();
            }
        });
    }



    /*private ButtonActionListener getButtonActionListener(){
        return view -> {
            int id = view.getId();
            switch (id){
                case R.id.notification_switch:
                    //viewModel.notificationStateChanged(((SwitchCompat) view).isChecked());
                    break;
                case R.id.update_button:
                    //viewModel.updateUserInfo();
                    break;
                case R.id.delete_button:
                    //viewModel.deleteUserFromDBRequest();
                    break;
                case R.id.photo_user:
                    chooseImageFromPhone();
                    break;
            }

        };
    }*/

    // --------------------
    // FOR ADD PHOTO
    // --------------------

    @AfterPermissionGranted(RC_IMAGE_PERMS)
    private void chooseImageFromPhone() {
        if(! EasyPermissions.hasPermissions(this, PERMS)){
            EasyPermissions.requestPermissions(
                    this, getString(R.string.need_permission_message), RC_IMAGE_PERMS, PERMS);
            return;
        }
        Log.e("here", "ehre");
        Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoIntent, RC_CHOOSE_PHOTO);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data){
        if(requestCode == RC_CHOOSE_PHOTO){
            if (resultCode == RESULT_OK){
                Uri uriImage = data.getData();
                if(uriImage != null) {
                    viewModel.updateUserPhoto(uriImage.toString());
                }
            }
        }

    }
}
