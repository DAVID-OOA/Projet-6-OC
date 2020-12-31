package com.oconte.david.go4lunch.workMates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.oconte.david.go4lunch.R;

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
