package com.oconte.david.go4lunch.workMates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.oconte.david.go4lunch.R;

import butterknife.ButterKnife;

public class FragmentWorkMates extends Fragment {



    public static FragmentWorkMates newInstance() {
        return (new FragmentWorkMates());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Available workmates");


        return view;
    }


}
