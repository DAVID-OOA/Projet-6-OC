package com.oconte.david.go4lunch.mapView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oconte.david.go4lunch.databinding.FragmentMapViewBinding;
import com.oconte.david.go4lunch.listView.ListRestaurantViewModel;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.util.PermissionUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class FragmentMapView extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float ZOOM_USER_LOCATION_VALUE = 15;

    private GoogleMap googleMap;
    private MapView mapView;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private List<Result> results;

    private FragmentMapViewBinding binding;

    private ListRestaurantViewModel viewModel;

    public static FragmentMapView newInstance() {
        return (new FragmentMapView());
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("I'm Hungry !");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        this.configureMapView(savedInstanceState);

        this.configureMapViewModel();

        return view;
    }

    public void getLocationPhone() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) getContext(), location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), ZOOM_USER_LOCATION_VALUE));
            }
        });
    }

    public void configureMapViewModel() {
        viewModel = new ViewModelProvider(this).get(ListRestaurantViewModel.class);

        viewModel.getRestaurantLiveData().observe(getViewLifecycleOwner(), results -> {

            FragmentMapView.this.results = results;
            if (googleMap != null) {
                for (Result result : results) {
                    Double latitude = result.getGeometry().getLocation().getLat();
                    Double longitude = result.getGeometry().getLocation().getLng();
                    LatLng positionRestaurant = new LatLng(latitude, longitude);

                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(positionRestaurant)
                            .title(result.getName()));
                    assert marker != null;
                    marker.setTag(result.getPlaceId());

                }
            }
        });
    }

    private void configureMapView(Bundle savedInstanceState) {
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        viewModel.getRestaurants();
        enableMyLocation();

        //Initial position for the camera change for  LatLng is userPosition
        getLocationPhone();

        // For zoom on map
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        this.displayRestaurantDetail();

    }

    public void displayRestaurantDetail(){
        googleMap.setOnInfoWindowClickListener(marker -> {
            Toast.makeText(getContext(), "It's ok", Toast.LENGTH_LONG).show();
            //Met a jour le live data
            String placeId = (String) marker.getTag();
            Result result = null;
            for (Result r : FragmentMapView.this.results) {
                if (r.getPlaceId().equals(placeId)) {
                    result = r;
                }
            }

            viewModel.selectRestaurant(result);
        });

    }

    ///////////////////////////////////////FOR LOCATION
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (googleMap!= null) {
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                   Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
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
