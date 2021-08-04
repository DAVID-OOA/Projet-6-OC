package com.oconte.david.go4lunch.listView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.FragmentListViewBinding;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.restoDetails.DetailsRestaurantActivity;
import com.oconte.david.go4lunch.util.ItemClickSupport;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class FragmentListViewRestaurant extends Fragment {

    private FragmentListViewBinding binding;

    private GooglePlaceNearByAdapter adapter;

    //private DetailsRestaurantViewModel viewModel;

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

        this.configureOnClickRecyclerView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void configureViewModel() {
        ListRestaurantViewModel viewModel = new ViewModelProvider(requireActivity()).get(ListRestaurantViewModel.class);
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

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(binding.fragmentMainRecyclerView, R.layout.detail_view_resto)
                .setOnItemClickListener((recyclerView, position, v) -> {

                    Intent intent = new Intent(getContext(), DetailsRestaurantActivity.class);
                    startActivity(intent);
                });

    }

}
