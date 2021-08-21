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
import com.oconte.david.go4lunch.models.DetailsRestaurant;
import com.oconte.david.go4lunch.models.Result;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailsRestaurantActivity extends AppCompatActivity {

    private DetailViewRestoBinding binding;

    private ListRestaurantViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailViewRestoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //this.configureDetailsView();

        this.configureViewModel();

    }

    public void configureDetailsView() {
        Intent intent = getIntent();
        DetailsRestaurant detailsRestaurant = (DetailsRestaurant) intent.getSerializableExtra("DetailsRestaurantObject");
        binding.nameRestaurant.setText(detailsRestaurant.getAdressRestaurant());
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this).get(ListRestaurantViewModel.class);
        viewModel.getSelectedRestaurant().observe(this, new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                if (result != null) {

                Picasso.get()
                        .load(getUrlPhoto(result))
                        .placeholder(R.drawable.go4lunch_icon)
                        .resize(60,60)
                        .into(binding.imageRestaurant);
                }

                binding.nameRestaurant.setText(Objects.requireNonNull(result).getName());

                binding.addressRestaurant.setText(result.getVicinity());
            }
        });
        viewModel.getRestaurants();
    }

    public String getUrlPhoto(Result result) {
        if (result.getPhotos() != null && result.getPhotos().size() > 0) {
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + result.getPhotos().get(0).getPhotoReference()+ "&key=AIzaSyCAxdxjPS79wAQ5WTz9FTtmvAfZXgIsOP8";
        }
        return null;
    }


}