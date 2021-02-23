package com.oconte.david.go4lunch.listView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.oconte.david.go4lunch.ApiDetailResponse;
import com.oconte.david.go4lunch.GooglePlaceCallRestaurantDetails;
import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.R;

import java.util.Objects;

import butterknife.ButterKnife;

public class FragmentListView extends Fragment implements GooglePlaceCallRestaurantDetails.Callbacks{

    public static FragmentListView newInstance() {
        return (new FragmentListView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("I'm Hungry !");

        this.executeHttpRequestWithRetrofitGooglePlaceDetails();

        return view;
    }


    private void executeHttpRequestWithRetrofitGooglePlaceDetails() {
        GooglePlaceCallRestaurantDetails restaurantDetails = Injection.getRestaurantDetail(Injection.getService(), Injection.resource);
        restaurantDetails.getRestaurantDetail(this,"placeId");
    }

    @Override
    public void onResponse(@Nullable ApiDetailResponse apiDetailResponse) {


    }

    @Override
    public void onFailure() {

    }
}
