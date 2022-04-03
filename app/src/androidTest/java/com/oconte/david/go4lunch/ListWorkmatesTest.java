package com.oconte.david.go4lunch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.oconte.david.go4lunch.injection.Injection;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class ListWorkmatesTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class, false, false);

    // Method allowing to outsource the creation of the mockwebserver object by passing the correct http code (200, 400 or other) and the correct json response.
    private MockWebServer setupServer(int code, String response) {
        MockWebServer server = new MockWebServer();
        server.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
                return new MockResponse().setResponseCode(code).setBody(response);
            }
        });
        return server;
    }

    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(Injection.resource);
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(Injection.resource);
    }

    @Test
    public void testCallsGooglePlaceMapVIew() throws IOException, InterruptedException {
        MockWebServer server = setupServer(HttpURLConnection.HTTP_OK, AssetReader.getAsset(InstrumentationRegistry.getInstrumentation().getContext(), "mapview_response.json"));

        // Start the server.
        server.start(9900);

        //Start the MainActivity
        mActivityRule.launchActivity(null);

        //Test recyclerview

        onView(withId(R.id.action_workmates)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.fragment_main_recycler_view)).perform();

        // Check one item of the recyclerview for see it here.
        onView(withId(R.id.fragment_main_recycler_view_workMates)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}
