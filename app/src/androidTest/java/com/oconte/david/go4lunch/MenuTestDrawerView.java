package com.oconte.david.go4lunch;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.oconte.david.go4lunch.auth.AuthActivity.EXTRA_IS_CONNECTED;

import android.content.SharedPreferences;
import android.view.Gravity;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class MenuTestDrawerView {



    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class, false, false);

    @Test
    public void testClickActionBarItemMenuDrawerLunch() throws IOException, InterruptedException {

        SharedPreferences preferences = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(EXTRA_IS_CONNECTED, true);
        editor.apply();

        //Start the MainActivity
        mActivityRule.launchActivity(null);

        //Select the toolbar
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));


        // Open Drawer to click on navigation.
        onView(withId(R.id.activity_main_drawerLayout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open());


        // Select the drawer menu for lunch button
        onView(withId(R.id.activity_main_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.activity_main_drawer_lunch));
        Thread.sleep(1000);

    }

    @Test
    public void testClickActionBarItemMenuDrawerSettings() throws IOException, InterruptedException {

        SharedPreferences preferences = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(EXTRA_IS_CONNECTED, true);
        editor.apply();

        //Start the MainActivity
        mActivityRule.launchActivity(null);

        //Select the toolbar
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));


        // Open Drawer to click on navigation.
        onView(withId(R.id.activity_main_drawerLayout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open());

        // Select the drawer menu for settings button
        onView(withId(R.id.activity_main_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.activity_main_drawer_settings));

    }

    @Test
    public void testClickActionBarItemMenuDrawerLogOut() throws IOException, InterruptedException {

        SharedPreferences preferences = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(EXTRA_IS_CONNECTED, true);
        editor.apply();

        //Start the MainActivity
        mActivityRule.launchActivity(null);

        //Select the toolbar
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));


        // Open Drawer to click on navigation.
        onView(withId(R.id.activity_main_drawerLayout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open());

        // Select the drawer menu for logout button
        onView(withId(R.id.activity_main_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.activity_main_drawer_logout));

    }

    @Test
    public void testClickBottomNavigationView() throws IOException, InterruptedException {

        SharedPreferences preferences = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences("EXTRA_LOG", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(EXTRA_IS_CONNECTED, true);
        editor.apply();

        //Start the MainActivity
        mActivityRule.launchActivity(null);

        // Select the bottom view menu for map button
        onView(withId(R.id.bottom_nav))
                .check(matches(isDisplayed()));

        onView(withId(R.id.action_map))
                .check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.action_list))
                .check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.action_workmates))
                .check(matches(isDisplayed())).perform(click());

    }

    @Test
    public void testClickActionBar() throws IOException, InterruptedException {

        //Start the MainActivity
        mActivityRule.launchActivity(null);

        //Select the toolbar
        onView(withId(R.id.toolbar)).perform(click());

    }

}
