package com.oconte.david.go4lunch.restodetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.DetailViewRestoBinding;
import com.oconte.david.go4lunch.listView.ListRestaurantViewModel;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.util.ForRating;
import com.squareup.picasso.Picasso;

public class DetailsRestaurantActivity extends AppCompatActivity {

    private DetailViewRestoBinding binding;

    ListRestaurantViewModel viewModel;

    private Resources res;
    boolean buttonOn;
    //UserRepository userRepository;
    //User user;

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

            this.configureOnClickPhoneButton(result);

            this.configureOnClickLikeButton();

            this.configureOnClickWebSite(result);

            this.configureOnClicFloatingButton();

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

    private void configureOnClicFloatingButton() {
        FloatingActionButton button = binding.pickRestaurantButton;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonOn) {
                    buttonOn = true;
                    binding.pickRestaurantButton.setBackgroundColor(Color.BLUE);
                } else {
                    buttonOn = false;
                    binding.pickRestaurantButton.setBackgroundColor(Color.GREEN);
                }

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configureOnClickLikeButton(){
        //Le passer sous un observer ?
        ImageButton button = binding.likeButton;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonOn) {
                    buttonOn = true;
                    binding.likeButton.setImageResource(R.drawable.star);

                } else {
                    buttonOn = false;
                    binding.likeButton.setImageResource(R.drawable.star_yellow);
                    viewModel.updateRestaurantLiked();
                }
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