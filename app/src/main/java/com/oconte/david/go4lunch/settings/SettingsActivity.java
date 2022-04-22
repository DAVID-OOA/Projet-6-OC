package com.oconte.david.go4lunch.settings;

import static com.oconte.david.go4lunch.auth.AuthActivity.EXTRA_IS_CONNECTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oconte.david.go4lunch.MainActivity;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.auth.AuthActivity;
import com.oconte.david.go4lunch.databinding.ActivitySettingsBinding;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.repositories.ViewModelFactory;
import com.oconte.david.go4lunch.util.TextUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingsActivity extends AppCompatActivity {

    //For Design
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;

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

        this.configureSettingsFactory();

        this.configureToolbar();

        this.switchButton();

        this.updateInfoOfUser();

        this.addPhotoUser();

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void switchButton() {
        SharedPreferences sharedPreferences = getSharedPreferences("state_of_switch", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        binding.notificationSwitch.setChecked(sharedPreferences.getBoolean("switch", false));

        binding.notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putBoolean("switch", true);
                    editor.apply();
                    startAlarmForWorkManager();
                    toast();
                    startMainActivity();
                } if (!isChecked){
                    editor.putBoolean("switch", false);
                    editor.apply();;

                }
                editor.apply();
            }
        });
    }

    /**
     * This is for said at WorkManager you start at 12 h and for all Day you work at this time.
     */
    @SuppressLint("NewApi")
    private void startAlarmForWorkManager() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * It's for custom Toast.
     */
    private void toast() {
        Toast toast = Toast.makeText(getBaseContext(), "The notification is ready", Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextSize(16);
        view.setBackgroundColor(Color.RED);
        toast.show();
    }

    public void configureSettingsFactory() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
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
    public void updateInfoOfUser() {
        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = binding.usernameField;
                if (!TextUtils.isEmpty(editText.getText().toString())){
                    String username = editText.getText().toString();
                    viewModel.updateUserName(username);
                }

                EditText editText1 = binding.emailField;
                if (!TextUtils.isEmpty(editText1.getText().toString())) {
                    String email = editText1.getText().toString();
                    viewModel.updateEmail(email);
                }
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FOR DELETE ACCOUNT
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.delete_button)
    public void onClickDeleteButton() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(R.string.popup_message_choice_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resultDeletedAccount();
                    }
                })
                .setNegativeButton(R.string.popup_message_choice_no, null)
                .show();
    }

    // It's for sign out and restart AuthActivity
    private void resultDeletedAccount() {
        this.deletedAccountUserFromFirebase();
    }

    // When you log out save the state for the next launch application.
    private void setIsDeconnected() {
        SharedPreferences preferences = getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(EXTRA_IS_CONNECTED, false);
        editor.apply();
    }

    // It's for sign Out
    private void deletedAccountUserFromFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String uid = user.getUid();
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        setIsDeconnected();
                        deleteUserAccount(uid);
                        startAuthActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Your auth is not worked, try again");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog, which) -> dialog.dismiss());
                        alertDialog.show();
                    }
                });
    }

    //For delete User
    public void deleteUserAccount(String uid) {
        if (uid == null) {
            return;
        }
        viewModel.deleteUser(uid);
    }

    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FOR ADD PHOTO
    public void addPhotoUser() {
        binding.photoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromPhone();
            }
        });
    }

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
                   viewModel.updatePhotoUser(String.valueOf(uriImage));
                }
            }
        }
    }
}
