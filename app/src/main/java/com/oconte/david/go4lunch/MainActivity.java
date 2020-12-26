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


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.bottom_nav) BottomNavigationView bottomNavigationView;
    @BindView(R.id.activity_main_drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView navigationView;


    //FOR FRAGMENTS
    // 1 - Declare fragment handled by Navigation Drawer
    private Fragment fragmentHome;
    private Fragment fragmentMapView;
    private Fragment fragmentListView;
    private Fragment fragmentWorkMates;
    private Fragment fragmentLangues;
    private Fragment fragmentInterests;

    //FOR DATAS
    // 2 - Identify each fragment with a number
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_MAP_VIEW = 1;
    private static final int FRAGMENT_LIST_VIEW = 2;
    private static final int FRAGMENT_LUNCH = 3;
    private static final int FRAGMENT_SETTINGS = 4;
    private static final int FRAGMENT_LOGOUT = 5;

    //FOR DATA
    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.startAuthActivity();

        this.configureToolbar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        this.configureBottomView();

        this.showFirstFragment();



    }

    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

    ////////////////////////////////////////////////////////////////////////
    // For auth
    ////////////////////////////////////////////////////////////////////////


    /*@OnClick(R.id.main_activity_button_login_google)
    public void onClickLoginButton() {
        // 3 - Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivity();
    }

    // 2 - Launch Sign-In Activity
    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_go4lunch_logo)
                        .build(),
                RC_SIGN_IN);
    }*/


    //////////////////////////////////////////////////////////////////
    // NAVIGATION DRAWER                                            //
    //////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     *  - Configure Drawer Layout
     */
    private void configureDrawerLayout(){
        //this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     *  - Configure NavigationView
     */
    private void configureNavigationView(){
        //this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                //this.showFragment(FRAGMENT_LOGOUT);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


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
        getSupportActionBar().setTitle("I'm Hungry");
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
        bottomNavigationView.setOnNavigationItemSelectedListener(item-> onNavigationItemSelected(item.getItemId()));
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // 1 - Show first fragment when activity is created
    private void showFirstFragment(){

        fragmentMapView = (FragmentMapView) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);

        if (fragmentMapView == null) {
            fragmentMapView = new FragmentMapView();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_frame_layout, fragmentMapView)
                    .commit();
        }

    }

    // 5 - Show fragment according an Identifier

    /*private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_MAP_VIEW:
                this.showMapViewFragment();
                break;
            case FRAGMENT_LIST_VIEW:
                this.showListViewFragment();
                break;
            default:
                break;
        }
    }*/

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

    //Fragment Navigation view

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}