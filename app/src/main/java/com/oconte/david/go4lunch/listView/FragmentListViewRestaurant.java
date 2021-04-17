package com.oconte.david.go4lunch.listView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.Result;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentListViewRestaurant extends Fragment {

    @BindView(R.id.fragment_main_recycler_view) RecyclerView recyclerView;

    private GooglePlaceNearByAdapter adapter;
    ApiNearByResponse apiNearByResponse;

    private ListRestaurantViewModel viewModel;

    public static FragmentListViewRestaurant newInstance() {
        return (new FragmentListViewRestaurant());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("I'm Hungry !");

        this.configureViewModel();

        this.configureRecyclerView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this).get(ListRestaurantViewModel.class);
        viewModel.getRestaurantLiveData().observe(getViewLifecycleOwner(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                adapter.updateCallRetrofitGoogleNearBy(results);
            }
        });
        viewModel.getRestaurants();
    }

    private void configureRecyclerView() {
        // Create adapter passing the list of articles
        this.adapter = new GooglePlaceNearByAdapter();

        // Attach the adapter to the recyclerView to populate items
        this.recyclerView.setAdapter(this.adapter);

        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
