package com.oconte.david.go4lunch.listView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.models.ApiNearByResponse;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentListView extends Fragment implements GooglePlaceCallRestaurantNearBy.Callbacks{

    //@BindView(R.id.list_view) TextView textView;
    //@BindView(R.id.testhttprequest) TextView textView;

    @BindView(R.id.fragment_main_recycler_view) RecyclerView recyclerView;

    private GooglePlaceNearByAdapter adapter;
    ApiNearByResponse apiNearByResponse;

    public static FragmentListView newInstance() {
        return (new FragmentListView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("I'm Hungry !");

        this.executeHttpRequestWithRetrofitGooglePlaceNearBy();

        this.configureRecyclerView();

        return view;
    }

    private void configureRecyclerView() {

        // Create adapter passing the list of articles
        this.adapter = new GooglePlaceNearByAdapter();

        // Attach the adapter to the recyclerView to populate items
        this.recyclerView.setAdapter(this.adapter);

        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void executeHttpRequestWithRetrofitGooglePlaceNearBy() {
        GooglePlaceCallRestaurantNearBy restaurantNearBy = Injection.getRestaurantNearBy(Injection.getService(), Injection.resource);
        restaurantNearBy.getRestaurantNearBy(this,"location");
    }


    @Override
    public void onResponse(@Nullable ApiNearByResponse apiNearByResponses) {
        this.apiNearByResponse = apiNearByResponses;
        this.adapter.updateCallRetrofitGoogleNearBy(apiNearByResponses);

    }


    @Override
    public void onFailure() {

    }
}
