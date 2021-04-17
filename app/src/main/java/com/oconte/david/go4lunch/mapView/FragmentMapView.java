package com.oconte.david.go4lunch.mapView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.FragmentMapViewBinding;
import com.oconte.david.go4lunch.listView.ListRestaurantViewModel;
import com.oconte.david.go4lunch.models.Geometry;
import com.oconte.david.go4lunch.models.Location;
import com.oconte.david.go4lunch.models.Result;

import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;

public class FragmentMapView extends Fragment implements OnMapReadyCallback {

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


    //@SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        viewModel.getRestaurants();


        //googleMap.setMyLocationEnabled(true);
        //googleMap.setOnMyLocationButtonClickListener(this);
        //googleMap.setOnMyLocationClickListener(this);

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
