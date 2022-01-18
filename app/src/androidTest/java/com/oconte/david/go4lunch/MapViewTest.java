package com.oconte.david.go4lunch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.MapView;
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

public class MapViewTest {

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

        MockWebServer server = setupServer(HttpURLConnection.HTTP_OK, AssetReader.getAsset(InstrumentationRegistry.getInstrumentation().getContext(), "mapview_respo,se.json"));

        // Start the server.
        server.start(9900);

        //Start the MainActivity
        mActivityRule.launchActivity(null);

        //Test recyclerview in map is good
        onView(withId(R.id.mapView)).perform();
    }
}
