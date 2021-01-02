package com.oconte.david.go4lunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import butterknife.ButterKnife;

public class FragmentListView extends Fragment {

    public static FragmentListView newInstance() {
        return (new FragmentListView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("I'm Hungry !");

        return view;
    }
}
