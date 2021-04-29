package com.oconte.david.go4lunch.workMates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.oconte.david.go4lunch.databinding.FragmentWorkmatesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FragmentWorkMates extends Fragment {


    public static FragmentWorkMates newInstance() {
        return (new FragmentWorkMates());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.oconte.david.go4lunch.databinding.FragmentWorkmatesBinding binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Available workmates");


        return view;
    }


}
