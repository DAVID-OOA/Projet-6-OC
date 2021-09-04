package com.oconte.david.go4lunch.restodetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.DetailViewRestoBinding;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.util.ForRating;
import com.squareup.picasso.Picasso;

public class DetailsRestaurantActivity extends AppCompatActivity {

    private DetailViewRestoBinding binding;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailViewRestoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        res = binding.detailRestaurantRootView.getResources();

        this.configureViewDetailsRestaurant();

    }

    public void configureViewDetailsRestaurant() {

        Intent intent = getIntent();
        Result result = (Result)intent.getSerializableExtra("result");
        if (result != null) {
            binding.nameRestaurant.setText(result.getName());

            binding.addressRestaurant.setText(result.getVicinity());

            Picasso.get()
                    .load(getUrlPhoto(result))
                    .placeholder(R.drawable.go4lunch_icon)
                    .into(binding.imageRestaurant);

            this.displayRating(result);
        }
    }

    public String getUrlPhoto(Result result) {
        if (result.getPhotos() != null && result.getPhotos().size() > 0) {
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + result.getPhotos().get(0).getPhotoReference()+ "&key=AIzaSyCAxdxjPS79wAQ5WTz9FTtmvAfZXgIsOP8";
        }
        return null;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void displayRating(Result result) {
        if (result.getRating() != null) {
            int rating = ForRating.calculateRating(result.getRating());
            binding.firstStar.setImageDrawable(res.getDrawable(ForRating.firstStar(rating)));
            binding.secondStar.setImageDrawable(res.getDrawable(ForRating.secondStar(rating)));
            binding.thirdStar.setImageDrawable(res.getDrawable(ForRating.thirdStar(rating)));
        }
    }


}