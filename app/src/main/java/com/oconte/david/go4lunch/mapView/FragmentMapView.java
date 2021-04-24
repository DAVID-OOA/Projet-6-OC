package com.oconte.david.go4lunch.mapView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oconte.david.go4lunch.MainActivity;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.FragmentMapViewBinding;
import com.oconte.david.go4lunch.listView.FragmentListViewRestaurant;
import com.oconte.david.go4lunch.listView.ListRestaurantViewModel;
import com.oconte.david.go4lunch.models.Geometry;
import com.oconte.david.go4lunch.models.Location;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.util.PermissionUtils;
import com.oconte.david.go4lunch.workMates.FragmentWorkMates;

import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;

public class FragmentMapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float ZOOM_USER_LOCATION_VALUE = 5;


    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = true;


    private GoogleMap googleMap;
    private MapView mapView;

    private FragmentMapViewBinding binding;

    private ListRestaurantViewModel viewModel;

    public static FragmentMapView newInstance() {
        return (new FragmentMapView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("I'm Hungry !");

        this.configureMapView(savedInstanceState);

        this.configureMapViewModel();

        /*if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }*/

        return view;
    }

    public void configureMapViewModel() {
        viewModel = new ViewModelProvider(this).get(ListRestaurantViewModel.class);

        viewModel.getRestaurantLiveData().observe(getViewLifecycleOwner(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {

                if (googleMap != null) {
                    for (Result result : results) {
                        Double latitude = result.getGeometry().getLocation().getLat();
                        Double longitude = result.getGeometry().getLocation().getLng();
                        LatLng positionRestaurant = new LatLng(latitude, longitude);

                        googleMap.addMarker(new MarkerOptions()
                                .position(positionRestaurant)
                                .title(result.getName()));

                    }
                }

            }
        });


    }

    private void configureMapView(Bundle savedInstanceState) {
        mapView = (MapView) binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        viewModel.getRestaurants();
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        //onMyLocationButtonClick();
        enableMyLocation();

        //Initial position for the camera change for  LatLng is userPosition
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.528331, 0.776410), ZOOM_USER_LOCATION_VALUE));

        // For zoom on map
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(getContext(), "It's ok", Toast.LENGTH_LONG).show();



        return true;
    }

    ///////////////////////////////////////FOR LOCATION
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (googleMap!= null) {
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            Toast.makeText(getContext(),"test", Toast.LENGTH_LONG).show();
            // Permission to access the location is missing. Show rationale and request permission
            //PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
              //      Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        /*Double latitude = 44.528331;
        Double longitude = 0.776410;
        LatLng myHome = new LatLng(latitude,longitude);
        CameraUpdateFactory.newLatLng(myHome);*/
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull android.location.Location location) {

        Toast.makeText(getContext(), "I'm here,", Toast.LENGTH_LONG).show();

        location.getLatitude();
        location.getLongitude();
        Double latitude = 44.528331;
        Double longitude = 0.776410;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }


    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "dialog");
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
