package com.oconte.david.go4lunch;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

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
        private Fragment fragmentCoordonnees;
        private Fragment fragmentExperiences;
        private Fragment fragmentEtudes;
        private Fragment fragmentLangues;
        private Fragment fragmentInterests;

        //FOR DATAS
        // 2 - Identify each fragment with a number
        private static final int FRAGMENT_HOME = 0;
        private static final int FRAGMENT_COORDONNEES = 1;
        private static final int FRAGMENT_EXPERIENCES = 2;
        private static final int FRAGMENT_LUNCH = 3;
        private static final int FRAGMENT_SETTINGS = 4;
        private static final int FRAGMENT_LOGOUT = 5;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);

            this.configureToolbar();

            this.configureDrawerLayout();

            this.configureNavigationView();

            this.configureBottomView();

            //this.showFirstFragment();
        }


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
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            // 4 - Handle Navigation Item Click
            int id = item.getItemId();

            switch (id){
                case R.id.activity_main_drawer_lunch:
                    this.showFragment(FRAGMENT_LUNCH);
                    break;
                case R.id.activity_main_drawer_settings:
                    this.startSettingsActivity();
                    return true;
                case R.id.activity_main_drawer_logout:
                    this.showFragment(FRAGMENT_LOGOUT);
                    break;
                default:
                    break;
            }

            this.drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        }

    /*private void startLunchActivity() {
        Intent intent = new Intent(this, SearchViewActivity.class);
        startActivity(intent);
    }*/

        private void startSettingsActivity(){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }

    /*private void startLogoutActivity(){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }*/


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
                    this.showFragment(FRAGMENT_LUNCH);
                    break;
                case R.id.action_list:
                    this.startSettingsActivity();
                    return true;
                case R.id.action_workmates:
                    this.showFragment(FRAGMENT_LOGOUT);
                    break;
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

            fragmentHome = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);

            if (fragmentHome == null) {
                fragmentHome = new HomeFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.activity_main_frame_layout, fragmentHome)
                        .commit();
            }

        }

        // 5 - Show fragment according an Identifier

        private void showFragment(int fragmentIdentifier){
            switch (fragmentIdentifier){
                //case FRAGMENT_HOME :
                //this.showHomeFragment();
                // break;
                case FRAGMENT_COORDONNEES:
                    //this.showCoordonneesFragment();
                    break;
                case FRAGMENT_EXPERIENCES:
                    //this.showExperiencesFragment();
                    break;
                case FRAGMENT_LUNCH:
                    //this.showEtudesFragment();
                    break;
                case FRAGMENT_SETTINGS:
                    //this.showLanguesFragment();
                    break;
                case FRAGMENT_LOGOUT:
                    //this.showInterestsFragment();
                    break;
                default:
                    break;
            }
        }

        // 4 - Create each fragment page and show it

    /*private void showHomeFragment() {
        if (this.fragmentHome == null) this.fragmentHome = CoordonneesFragment.newInstance();
        this.startTransactionFragment(this.fragmentHome);
    }

    private void showCoordonneesFragment(){
        if (this.fragmentCoordonnees == null) this.fragmentCoordonnees = CoordonneesFragment.newInstance();
        this.startTransactionFragment(this.fragmentCoordonnees);
    }*/

        // 3 - Generic method that will replace and show a fragment inside the MainActivity Frame Layout
        private void startTransactionFragment(Fragment fragment) {
            if (!fragment.isVisible()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_frame_layout, fragment).commit();
            }
        }

        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {

        }
}