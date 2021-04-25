package com.oconte.david.go4lunch.restoDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.mapView.FragmentMapView;

import java.util.Objects;

import butterknife.ButterKnife;

public class FragmentDetailsRestaurant extends Fragment {


    public static FragmentDetailsRestaurant newInstance() {
        return (new FragmentDetailsRestaurant());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, view);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Available workmates");


        return view;
    }
}
