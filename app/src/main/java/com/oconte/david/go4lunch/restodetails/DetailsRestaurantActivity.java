package com.oconte.david.go4lunch.restodetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.DetailViewRestoBinding;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.util.ForRating;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailsRestaurantActivity extends AppCompatActivity {

    private DetailViewRestoBinding binding;
    public DetailsRestaurantViewModel viewModel;

    Result result;

    String idRestaurant;

    private Resources res;
    boolean buttonOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailViewRestoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.configureViewDetailsRestaurantFactory(FirebaseAuth.getInstance(),FirebaseFirestore.getInstance());

        res = binding.detailRestaurantRootView.getResources();

        Intent intent = getIntent();

        result = (Result)intent.getSerializableExtra("result");

        this.configureViewDetailsRestaurant(result);

    }

    public void configureViewDetailsRestaurantFactory(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        ViewModelDetailsRestaurantFactory viewModelDetailsRestaurantFactory = Injection.provideViewModelFactory(firebaseAuth,firebaseFirestore);
        ViewModelProvider viewModelProvider = new ViewModelProvider(DetailsRestaurantActivity.this, viewModelDetailsRestaurantFactory);
        viewModel = viewModelProvider.get(DetailsRestaurantViewModel.class);
    }

    public void configureViewDetailsRestaurant(Result result) {

        if (result != null) {
            binding.nameRestaurant.setText(result.getName());

            binding.addressRestaurant.setText(result.getVicinity());

            Picasso.get()
                    .load(getUrlPhoto(result))
                    .placeholder(R.drawable.go4lunch_icon)
                    .into(binding.imageRestaurant);

            idRestaurant = Objects.requireNonNull(result).getPlaceId();
            //user = userRepository.getCurrentUser();

            this.displayRating(result);

            this.configureOnClickLikeButton();

            this.configureOnClickPhoneButton(result);

            this.configureOnClickWebSite(result);

            this.configureOnPickedButton();

            this.conditionButtonLikedClick();
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

    private void configureOnPickedButton() {
        binding.pickRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onPickedOnButtonClick(idRestaurant);
                // appel juste le viewModel
                if (!buttonOn) {
                    buttonOn = true;
                    binding.pickRestaurantButton.setImageResource(R.drawable.floatingbuttonoff);
                } else {
                    buttonOn = false;
                    binding.pickRestaurantButton.setImageResource(R.drawable.floatingbutton);
                }
            }
        });
    }

    private void conditionButtonLikedClick() {
        viewModel.getDataRestaurantClick(idRestaurant);
        viewModel.getRestaurantsLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        binding.likeButton.setImageResource(R.drawable.star_yellow);
                        Log.d("TAG","switch on yellow");

                    } else {
                        binding.likeButton.setImageResource(R.drawable.star);
                        Log.d("TAG","switch on black");
                    }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configureOnClickLikeButton(){
        binding.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onLikedOnButtonClick(idRestaurant);
            }
        });
    }

    private void configureOnClickPhoneButton(Result result) {
        binding.phoneButton.setOnClickListener(v -> {
            if (result.getPhoneNumber() != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(result.getPhoneNumber()));
                startActivity(intent);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(DetailsRestaurantActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("No Phone Number are find.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
        });
    }

    private void configureOnClickWebSite(Result result) {
        binding.websiteButton.setOnClickListener(v -> {
            if (result.getWebsite() != null) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", result.getWebsite());
                startActivity(intent);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(DetailsRestaurantActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("No WebSite are find.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
        });
    }
}