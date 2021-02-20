package com.oconte.david.go4lunch.mapView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.FragmentMapViewBinding;

import java.util.Objects;

import butterknife.ButterKnife;

public class FragmentMapView extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;

    private FragmentMapViewBinding binding;

    public static FragmentMapView newInstance() {
        return (new FragmentMapView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("I'm Hungry !");

        this.configureMapView(savedInstanceState);

        return view;
    }

    private void configureMapView(Bundle savedInstanceState) {
        mapView = (MapView) binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

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
