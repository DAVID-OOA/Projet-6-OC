package com.oconte.david.go4lunch;

import static com.oconte.david.go4lunch.auth.AuthActivity.EXTRA_IS_CONNECTED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oconte.david.go4lunch.auth.AuthActivity;
import com.oconte.david.go4lunch.databinding.ActivityMainBinding;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.listView.FragmentListViewRestaurant;
import com.oconte.david.go4lunch.listView.ListRestaurantViewModel;
import com.oconte.david.go4lunch.lunch.LunchActivity;
import com.oconte.david.go4lunch.mapView.FragmentMapView;
import com.oconte.david.go4lunch.models.PlaceTestForAutocompleteToDetails;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.restodetails.DetailsRestaurantActivity;
import com.oconte.david.go4lunch.repositories.ViewModelFactory;
import com.oconte.david.go4lunch.settings.SettingsActivity;
import com.oconte.david.go4lunch.util.ForPosition;
import com.oconte.david.go4lunch.workMates.FragmentWorkMates;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    String myApiKey = BuildConfig.MAPS_API_KEY;

    //FOR FRAGMENTS
    private Fragment fragmentMapView;
    private Fragment fragmentListView;
    private Fragment fragmentWorkMates;

    public ListRestaurantViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ButterKnife.bind(this);

        this.checkLogOrNotLog();

        this.configureViewDetailsRestaurantFactory();

        this.configurationViewModelDetails();

        this.updateUIWithUserData();

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), myApiKey);
        }

        // For Fragment
        this.showFirstFragment();

        // For UI
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomView();
    }

    public void configureViewDetailsRestaurantFactory() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(MainActivity.this, viewModelFactory);
        viewModel = viewModelProvider.get(ListRestaurantViewModel.class);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // For search
    ActivityResultLauncher<Intent> searchResultLuncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()== RESULT_OK) {

                    boolean isRestaurant = false;
                    Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(result.getData()));
                    if(place.getTypes() != null) {

                        for (Place.Type type : place.getTypes()) {
                            if (type == Place.Type.RESTAURANT) {
                                isRestaurant = true;
                                break;
                            }
                        }
                    }
                    if(isRestaurant || place.getTypes() == null) {

                        String name = place.getName();
                        String address = place.getAddress();
                        Double rating = place.getRating();
                        String webSite = String.valueOf(place.getWebsiteUri());
                        String idRestaurant = place.getId();
                        String phoneNumber = place.getPhoneNumber();

                        List<PhotoMetadata> metadata = place.getPhotoMetadatas();

                        PlaceTestForAutocompleteToDetails placeTestForAutocompleteToDetails = new PlaceTestForAutocompleteToDetails(name, address, rating, webSite, idRestaurant, phoneNumber, metadata);

                        Intent intent = new Intent(getApplicationContext(), DetailsRestaurantActivity.class);
                        intent.putExtra("placeTestForAutocompleteToDetails", placeTestForAutocompleteToDetails);
                        startActivity(intent);
                    }

                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(result.getData()));
                    Toast.makeText(getApplicationContext(), "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();

                } else if (result.getResultCode() == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
    });

    public void onSearchCalled() {
        LatLng position = viewModel.getMyLocation();
        LatLngBounds bounds = ForPosition.convertToBounds(position, 2500);

        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.PHOTO_METADATAS,
                Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI,
                Place.Field.RATING);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setLocationRestriction(RectangularBounds.newInstance(bounds.southwest, bounds.northeast))
                .build(this);
        searchResultLuncher.launch(intent);
    }



    //////////////////////////////////////////////////////////
    // For see if log or not
    private void checkLogOrNotLog() {
        SharedPreferences preferences = getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        boolean resultLogging = preferences.getBoolean(EXTRA_IS_CONNECTED, false);
        if (!resultLogging) {
            this.startAuthActivity();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // For starts activities
    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    public void configurationViewModelDetails() {
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //FOR SIGN OUT

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
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Your auth is not worked, try again");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog, which) -> dialog.dismiss());
                        alertDialog.show();
                    }
                });
    }

    //For delete User
    public void deleteUser(String uid) {
        if (uid == null) {
            return;
        }
        viewModel.deleteUser(uid);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // For setting activity
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main_activity_search) {
            onSearchCalled();
            return true;
        }
        return false;
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
                this.startLunchActivity();
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

    private void startLunchActivity() {
        Intent intent = new Intent(this, LunchActivity.class);
        startActivity(intent);
    }


    ////////////////////////////////////////////////////
    // BOTTOM MENU
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
        binding.bottomNav.setOnItemSelectedListener(item -> onNavigationItemSelected(item.getItemId()));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FRAGMENTS

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
        if (this.fragmentMapView == null)
            this.fragmentMapView = FragmentMapView.newInstance();
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //For Data UserConnected --> menu drawer
    private void updateUIWithUserData() {
        if (viewModel.isCurrentUserLogged()) {

            FirebaseUser currentUser = viewModel.isForGetCurrentUser();

            viewModel.getUserInfoConnecteds().observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if(currentUser.getUid().equals(user.getUid())) {
                        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
                        @SuppressLint("ResourceType")
                        View headerView = navigationView.getHeaderView(0);
                        TextView username = headerView.findViewById(R.id.nav_header_username);
                        username.setText(TextUtils.isEmpty(Objects.requireNonNull(user).getUsername()) ? getString(R.string.info_no_username_found) : user.getUsername());

                        if (user.getUrlPicture() != null) {
                            ImageView imageUser = headerView.findViewById(R.id.imageview_header_navigationview);
                            Picasso.get().
                                    load(user.getUrlPicture())
                                    .transform(new CropCircleTransformation())
                                    .placeholder(R.drawable.baseline_account_circle_24)
                                    .into(imageUser);
                        }

                        TextView useremail = headerView.findViewById(R.id.nav_header_email);
                        useremail.setText(TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail());
                    }
                }
            });
            viewModel.getUserInfoConnected();
        }
    }

}