package com.oconte.david.go4lunch.listView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.FragmentListViewBinding;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.restodetails.DetailsRestaurantActivity;
import com.oconte.david.go4lunch.util.ForNetIsAvailable;
import com.oconte.david.go4lunch.util.ItemClickSupport;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class FragmentListViewRestaurant extends Fragment {

    private FragmentListViewBinding binding;
    private GooglePlaceNearByAdapter adapter;
    List<Result> results;

    public static FragmentListViewRestaurant newInstance() {
        return (new FragmentListViewRestaurant());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("I'm Hungry !");

        this.configureRecyclerView();
        this.configureViewModel();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void configureViewModel() {
        if (!ForNetIsAvailable.isNetworkConnected(requireContext())) {
            AlertDialog alertDialog = new AlertDialog.Builder(requireActivity()).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("There is no internet connection");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            return;
        }
        ListRestaurantViewModel viewModel = new ViewModelProvider(requireActivity()).get(ListRestaurantViewModel.class);
        viewModel.getRestaurantLiveData().observe(getViewLifecycleOwner(), results -> {
            adapter.updateCallRetrofitGoogleNearBy(results);
            FragmentListViewRestaurant.this.results = results;
            configureOnClickRecyclerView(results);
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

    private void configureOnClickRecyclerView(List<Result> results){
        ItemClickSupport.addTo(binding.fragmentMainRecyclerView, R.layout.detail_view_resto)
                .setOnItemClickListener((recyclerView, position, v) -> {

            String placeId = results.get(position).getPlaceId();

            Result result = null;

            for (Result r : FragmentListViewRestaurant.this.results) {
                if (r.getPlaceId().equals(placeId)){
                    result = r;
                    break;
                }
            }

            Intent intent = new Intent(requireActivity(), DetailsRestaurantActivity.class);
            intent.putExtra("result", result);
            startActivity(intent);
        });
    }
}