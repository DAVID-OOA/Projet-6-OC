package com.oconte.david.go4lunch;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.oconte.david.go4lunch.databinding.ActivityBrunchBinding;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.listView.ListRestaurantViewModel;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.repositories.ViewModelFactory;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

public class LunchActivity extends AppCompatActivity {

    private ActivityBrunchBinding binding;

    public LunchViewModel viewModel;



    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBrunchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ButterKnife.bind(this);

        this.configureViewLunchActivitytFactory();

        this.configureViewDetailRestaurantPicked();

        this.dataViewDetailRestaurantPicked();

    }

    public void configureViewLunchActivitytFactory() {
        uid = FirebaseAuth.getInstance().getUid();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(LunchActivity.this, viewModelFactory);
        viewModel = viewModelProvider.get(LunchViewModel.class);
    }

    public void configureViewDetailRestaurantPicked() {
        viewModel.getUserRestaurantPicked(uid);

    }

    public void dataViewDetailRestaurantPicked() {
        viewModel.getLunchRestaurantPickedData().observe(this, new Observer<Restaurant>() {
            @Override
            public void onChanged(Restaurant restaurant) {
                binding.nameRestaurant.setText(restaurant.getUsername());

                binding.addressRestaurant.setText(restaurant.getAddressRestaurant());

                Picasso.get()
                        .load(restaurant.getUrlPicture())
                        .placeholder(R.drawable.go4lunch_icon)
                        .into(binding.imageRestaurant);



            }
        });
    }
}
