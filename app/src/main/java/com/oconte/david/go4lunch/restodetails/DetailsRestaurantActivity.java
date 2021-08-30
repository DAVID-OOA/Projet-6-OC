package com.oconte.david.go4lunch.restodetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.DetailViewRestoBinding;
import com.oconte.david.go4lunch.listView.ListRestaurantViewModel;
import com.oconte.david.go4lunch.models.Result;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailsRestaurantActivity extends AppCompatActivity {

    private DetailViewRestoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailViewRestoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.configureViewDetailsRestaurant();

    }

    public void configureViewDetailsRestaurant() {

        Intent intent = getIntent();
        Result result = (Result)intent.getSerializableExtra("result");

        binding.nameRestaurant.setText(result.getName());

        binding.addressRestaurant.setText(result.getVicinity());

        Picasso.get()
                .load(getUrlPhoto(result))
                .placeholder(R.drawable.go4lunch_icon)
                .into(binding.imageRestaurant);


    }

    public String getUrlPhoto(Result result) {
        if (result.getPhotos() != null && result.getPhotos().size() > 0) {
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + result.getPhotos().get(0).getPhotoReference()+ "&key=AIzaSyCAxdxjPS79wAQ5WTz9FTtmvAfZXgIsOP8";
        }
        return null;
    }


}