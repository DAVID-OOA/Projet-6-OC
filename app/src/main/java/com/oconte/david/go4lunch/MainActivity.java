package com.oconte.david.go4lunch;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.oconte.david.go4lunch.auth.AuthActivity;
import com.oconte.david.go4lunch.databinding.ActivityMainBinding;
import com.oconte.david.go4lunch.workMates.FragmentWorkMates;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) Toolbar toolbar;


    private ActivityMainBinding binding;

    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    //FOR FRAGMENTS
    // 1 - Declare fragment handled by Navigation Drawer
    private Fragment fragmentHome;
    private Fragment fragmentMapView;
    private Fragment fragmentListView;
    private Fragment fragmentWorkMates;
    private Fragment fragmentLangues;
    private Fragment fragmentInterests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ButterKnife.bind(this);

        this.startAuthActivity();

        this.configureToolbar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        this.configureBottomView();

        this.showFirstFragment();

        this.setUpForStartThisActivity();

    }


    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }


    //It's for start after the authentification
    public void setUpForStartThisActivity(){
        Intent i = new Intent(this, AuthActivity.class);
        startActivityForResult(i,RC_SIGN_IN);
    }

    //////////////////////////////////////////////////////////////////
    // NAVIGATION DRAWER                                            //
    //////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (binding.activityMainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     *  - Configure Drawer Layout
     */
    private void configureDrawerLayout(){
        //this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.activityMainDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.activityMainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     *  - Configure NavigationView
     */
    private void configureNavigationView(){
        //this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        binding.activityMainNavView.setNavigationItemSelectedListener(this);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
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

    ///////////////////////////////////////////////////////////////////////////////////////
    //For SIGN OUT
    ///////////////////////////////////////////////////////////////////////////////////////

    // It's for sign out and restart AuthActivity
    private void resultSignOut() {
        this.signOutUserFromFirebase();
        this.startAuthActivity();
    }

    // It's for sign Out
    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////

    private void startSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // ---------------------
    // TOOLBAR
    // ---------------------

    /**
     *  - Configure the Toolbar
     */
    protected void configureToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("I'm Hungry !");
    }


    ////////////////////////////////////////////////////
    // BOTTOM MENU
    ////////////////////////////////////////////////////
    /**
     *  - Configure BottomView
     */
    public boolean onNavigationItemSelected(Integer integer) {

        switch (integer){
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
        binding.bottomNav.setOnNavigationItemSelectedListener(item-> onNavigationItemSelected(item.getItemId()));
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show first fragment when activity is created
    private void showFirstFragment(){

        fragmentMapView = (FragmentMapView) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);

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
    private void showMapViewFragment(){
        if (this.fragmentMapView == null) this.fragmentMapView = FragmentMapView.newInstance();
        this.startTransactionFragment(this.fragmentMapView);
    }

    private void showListViewFragment(){
        if (this.fragmentListView == null) this.fragmentListView = FragmentListView.newInstance();
        this.startTransactionFragment(this.fragmentListView);
    }

    private void showWorkMatesFragment(){
        if (this.fragmentWorkMates == null) this.fragmentWorkMates = FragmentWorkMates.newInstance();
        this.startTransactionFragment(this.fragmentWorkMates);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}