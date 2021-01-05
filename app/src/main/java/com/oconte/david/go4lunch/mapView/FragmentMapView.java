package com.oconte.david.go4lunch.mapView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.oconte.david.go4lunch.R;

import java.util.Objects;

import butterknife.ButterKnife;

public class FragmentMapView extends Fragment {

    public static FragmentMapView newInstance() {
        return (new FragmentMapView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        ButterKnife.bind(this, view);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("I'm Hungry !");

        return view;
    }
}
