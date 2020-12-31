package com.oconte.david.go4lunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentWorkMates extends Fragment {


    //@BindView(R.id.toolbar) Toolbar toolbar;

    public static FragmentWorkMates newInstance() {
        return (new FragmentWorkMates());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, view);

        //toolbar.setTitle("Available workmates");

        return view;
    }


}
