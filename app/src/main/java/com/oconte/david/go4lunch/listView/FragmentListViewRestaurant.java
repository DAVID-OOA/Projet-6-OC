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

import com.oconte.david.go4lunch.databinding.FragmentListViewBinding;
import com.oconte.david.go4lunch.models.Result;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class FragmentListViewRestaurant extends Fragment {

    private FragmentListViewBinding binding;

    private GooglePlaceNearByAdapter adapter;




    public static FragmentListViewRestaurant newInstance() {
        return (new FragmentListViewRestaurant());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         binding = FragmentListViewBinding.inflate(inflater, container, false);
         View view = binding.getRoot();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("I'm Hungry !");

        this.configureViewModel();

        this.configureRecyclerView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void configureViewModel() {
        ListRestaurantViewModel viewModel = new ViewModelProvider(this).get(ListRestaurantViewModel.class);
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
        this.binding.fragmentMainRecyclerView.setAdapter(this.adapter);

        // Set layout manager to position the items
        this.binding.fragmentMainRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
