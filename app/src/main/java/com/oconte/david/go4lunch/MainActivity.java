package com.oconte.david.go4lunch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oconte.david.go4lunch.auth.AuthActivity;
import com.oconte.david.go4lunch.databinding.ActivityMainBinding;
import com.oconte.david.go4lunch.listView.FragmentListViewRestaurant;
import com.oconte.david.go4lunch.listView.ListRestaurantViewModel;
import com.oconte.david.go4lunch.mapView.FragmentMapView;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.restoDetails.DetailsRestaurantActivity;
import com.oconte.david.go4lunch.workMates.FragmentWorkMates;
import com.oconte.david.go4lunch.workMates.UserRepository;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.content.ContentValues.TAG;
import static com.oconte.david.go4lunch.auth.AuthActivity.EXTRA_IS_CONNECTED;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    private ListRestaurantViewModel viewModel;

    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    //FOR FRAGMENTS
    private Fragment fragmentMapView;
    private Fragment fragmentListView;
    private Fragment fragmentWorkMates;

    // For firebase
    //private static volatile MainActivity instance;
    private final UserRepository userRepository;

    public MainActivity() {
        userRepository = UserRepository.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ButterKnife.bind(this);

        // For UI
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomView();

        // For Fragment
        this.showFirstFragment();

        this.checkLogOrNotLog();

        this.configurationViewModelDetails();

        this.updateUIWithUserData();

    }

    ///////////////////////////////////////////////////////////
    public void configurationViewModelDetails() {
        viewModel = new ViewModelProvider(this).get(ListRestaurantViewModel.class);
        viewModel.getSelectedRestaurant().observe(this, new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                if (result != null) {
                    Intent intent = new Intent(MainActivity.this, DetailsRestaurantActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //////////////////////////////////////////////////////////
    private void checkLogOrNotLog() {
        SharedPreferences preferences = getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        boolean resultLogging = preferences.getBoolean(EXTRA_IS_CONNECTED, false);
        if (!resultLogging) { //siginfie que si ce boolean est faux. equivaux a resultLogging == false
            this.startAuthActivity();
            finish();
        }
    }

    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }


    //For SIGN OUT

    // It's for sign out and restart AuthActivity
    private void resultSignOut() {
        this.signOutUserFromFirebase();

    }

    // When you log out save the state for the next launch application.
    private void setIsDeconnected() {
        SharedPreferences preferences = getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(EXTRA_IS_CONNECTED, false);
        editor.apply();
    }


    // It's for sign Out
    private void signOutUserFromFirebase() {
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
                        deleteUser(uid);
                        startAuthActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    /*For delete User*/
    public void deleteUser(String uid) {
        if (uid == null) {
            return;
        }
        userRepository.deleteUserFromFirestore(uid);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //////////////////////////// UI ///////////////////////////////////////////////////////////////

    /**
     * Configure the Toolbar
     */
    protected void configureToolbar() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("I'm Hungry !");
    }

    // NAVIGATION DRAWER
    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (binding.activityMainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Configure Drawer Layout
     */
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.activityMainDrawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.activityMainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Configure NavigationView
     */
    private void configureNavigationView() {
        binding.activityMainNavView.setNavigationItemSelectedListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_lunch:
                //this.showFragment(FRAGMENT_LUNCH);
                break;
            case R.id.activity_main_drawer_settings:
                this.startSettingsActivity();
                return true;
            case R.id.activity_main_drawer_logout:
                this.resultSignOut();
                break;
            default:
                break;
        }

        binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    ////////////////////////////////////////////////////
    // BOTTOM MENU
    ////////////////////////////////////////////////////

    /**
     * Configure BottomView
     */
    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(Integer integer) {

        switch (integer) {
            case R.id.action_map:
                this.showMapViewFragment();
                break;
            case R.id.action_list:
                this.showListViewFragment();
                return true;
            case R.id.action_workmates:
                this.showWorkMatesFragment();
            default:
                break;
        }

        return true;
    }

    private void configureBottomView() {
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> onNavigationItemSelected(item.getItemId()));
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show first fragment when activity is created
    private void showFirstFragment() {
        fragmentMapView = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (fragmentMapView == null) {
            fragmentMapView = new FragmentMapView();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_frame_layout, fragmentMapView)
                    .commit();
        }
    }

    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }

    //Fragment bottom view
    private void showMapViewFragment() {
        if (this.fragmentMapView == null) this.fragmentMapView = FragmentMapView.newInstance();
        this.startTransactionFragment(this.fragmentMapView);
    }

    private void showListViewFragment() {
        if (this.fragmentListView == null)
            this.fragmentListView = FragmentListViewRestaurant.newInstance();
        this.startTransactionFragment(this.fragmentListView);
    }

    private void showWorkMatesFragment() {
        if (this.fragmentWorkMates == null)
            this.fragmentWorkMates = FragmentWorkMates.newInstance();
        this.startTransactionFragment(this.fragmentWorkMates);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    //For Data UserCOnnected
    private void updateUIWithUserData() {
        if (userRepository.isCurrentUserLogged()) {
            FirebaseUser currentUser = userRepository.getCurrentUser();

            NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
            @SuppressLint("ResourceType")
            View headerView = navigationView.getHeaderView(0);
            TextView username = headerView.findViewById(R.id.nav_header_username);
            username.setText(TextUtils.isEmpty(Objects.requireNonNull(currentUser).getDisplayName()) ? getString(R.string.info_no_username_found) : currentUser.getDisplayName());

            if (currentUser.getPhotoUrl() != null) {
                ImageView imageUser = headerView.findViewById(R.id.imageview_header_navigationview);
                Picasso.get().
                        load(currentUser.getPhotoUrl())
                        .transform(new CropCircleTransformation())
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .into(imageUser);
            }

            TextView useremail = headerView.findViewById(R.id.nav_header_email);
            useremail.setText(TextUtils.isEmpty(currentUser.getEmail()) ? getString(R.string.info_no_username_found) : currentUser.getEmail());
        }
    }

}