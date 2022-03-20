package com.oconte.david.go4lunch.restodetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.DetailViewRestoBinding;
import com.oconte.david.go4lunch.models.PlaceTestForAutocompleteToDetails;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.repositories.ViewModelFactory;
import com.oconte.david.go4lunch.util.ForRating;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DetailsRestaurantActivity extends AppCompatActivity {

    private DetailViewRestoBinding binding;
    public DetailsRestaurantViewModel viewModel;

    private DetailsRestaurantPickedByUserAdapter detailsRestaurantPickedByUserAdapter;

    Result result;
    String idRestaurant;
    String uid;
    String nameRestaurantPicked;
    String adressRestaurantPicked;
    String photoUrlRestaurantpicked;

    PlaceTestForAutocompleteToDetails placeTestForAutocompleteToDetails;

    private PlacesClient placesClient;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailViewRestoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        placesClient = Places.createClient(this);

        this.configureViewDetailsRestaurantFactory();

        res = binding.detailRestaurant.getResources();

        Intent intent = getIntent();

        if (intent.hasExtra("result")) {
            result = (Result)intent.getSerializableExtra("result");
            this.configureViewDetailsRestaurant(result);
        } else {
            placeTestForAutocompleteToDetails = (PlaceTestForAutocompleteToDetails) intent.getSerializableExtra("placeTestForAutocompleteToDetails");
            this.configureViewDetailsRestaurantByPlace(placeTestForAutocompleteToDetails);
        }
    }

    public void configureViewDetailsRestaurantFactory() {
        uid = FirebaseAuth.getInstance().getUid();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(DetailsRestaurantActivity.this, viewModelFactory);
        viewModel = viewModelProvider.get(DetailsRestaurantViewModel.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // For Details with Place
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void configureViewDetailsRestaurantByPlace(PlaceTestForAutocompleteToDetails placeTestForAutocompleteToDetails) {
        if (placeTestForAutocompleteToDetails != null ) {
            binding.nameRestaurant.setText(placeTestForAutocompleteToDetails.getName());

            binding.addressRestaurant.setText(placeTestForAutocompleteToDetails.getAdress());

            idRestaurant = placeTestForAutocompleteToDetails.getIdRestaurant();

            this.getPlacePhoto();
            this.displayRatingForPlace(placeTestForAutocompleteToDetails);
            this.configureOnClickLikeButton();
            this.configureOnClickPhoneButtonForPlace(placeTestForAutocompleteToDetails);
            this.configureOnClickWebSiteForPlace(placeTestForAutocompleteToDetails);
            this.configureOnPickedButton();
            this.conditionButtonLikedClick();
            this.conditionButtonPickedClick();
            this.configureViewModelForRecyclerViewUserPickedRestaurant();
            this.configureRecyclerView();

        }
    }

    private void getPlacePhoto() {
        final String placeid = idRestaurant;

        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);

        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeid, fields);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            final Place place = response.getPlace();

            // Get the photo metadata.
            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
            if (metadata == null || metadata.isEmpty()) {
                Log.w("TAG", "No photo metadata.");
                return;
            }
            final PhotoMetadata photoMetadata = metadata.get(0);

            // Get the attribution text.
            final String attributions = photoMetadata.getAttributions();

            // Create a FetchPhotoRequest.
            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                binding.imageRestaurant.setImageBitmap(bitmap);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e("TAG", "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();
                    // TODO: Handle error with given status code.
                }
            });
        });
    }

    private void configureOnClickPhoneButtonForPlace(PlaceTestForAutocompleteToDetails placeTestForAutocompleteToDetails) {
        binding.phoneButton.setOnClickListener(v -> {
            if (placeTestForAutocompleteToDetails.getPhoneNumber() != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + placeTestForAutocompleteToDetails.getPhoneNumber()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this,"ca ne marche pas", Toast.LENGTH_LONG).show();
                }


            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(DetailsRestaurantActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("No Phone Number are find.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
        });
    }

    private void configureOnClickWebSiteForPlace(PlaceTestForAutocompleteToDetails placeTestForAutocompleteToDetails) {
        binding.websiteButton.setOnClickListener(v -> {
            if (placeTestForAutocompleteToDetails.getWebSite() != null) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url2", placeTestForAutocompleteToDetails.getWebSite());
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void displayRatingForPlace(PlaceTestForAutocompleteToDetails placeTestForAutocompleteToDetails) {
        if (placeTestForAutocompleteToDetails.getRating() != null) {
            int rating = ForRating.calculateRating(placeTestForAutocompleteToDetails.getRating());
            binding.firstStar.setImageDrawable(res.getDrawable(ForRating.firstStar(rating)));
            binding.secondStar.setImageDrawable(res.getDrawable(ForRating.secondStar(rating)));
            binding.thirdStar.setImageDrawable(res.getDrawable(ForRating.thirdStar(rating)));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // For Details with Result
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void configureViewDetailsRestaurant(Result result) {
        if (result != null) {
            binding.nameRestaurant.setText(result.getName());

            binding.addressRestaurant.setText(result.getVicinity());

            Picasso.get()
                    .load(getUrlPhoto(result))
                    .placeholder(R.drawable.go4lunch_icon)
                    .into(binding.imageRestaurant);

            idRestaurant = Objects.requireNonNull(result).getPlaceId();
            nameRestaurantPicked = result.getName();
            adressRestaurantPicked = result.getVicinity();
            photoUrlRestaurantpicked = getUrlPhoto(result);

            this.displayRating(result);
            this.configureOnClickLikeButton();
            this.configureOnClickPhoneButton(result);
            this.configureOnClickWebSite(result);
            this.configureOnPickedButton();
            this.conditionButtonLikedClick();
            this.conditionButtonPickedClick();
            this.configureViewModelForRecyclerViewUserPickedRestaurant();
            this.configureRecyclerView();

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
                viewModel.onPickedOnButtonClick(idRestaurant, uid, nameRestaurantPicked, adressRestaurantPicked, photoUrlRestaurantpicked);
            }
        });
    }

    private void conditionButtonPickedClick() {
        viewModel.getDataRestaurantPickedClick(idRestaurant);
        viewModel.getRestaurantsPickedLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.pickRestaurantButton.setImageResource(R.drawable.floatingbuttonoff);
                } else {
                    binding.pickRestaurantButton.setImageResource(R.drawable.floatingbutton);
                }
                configureViewModelForRecyclerViewUserPickedRestaurant();
            }
        });
    }

    private void configureOnClickLikeButton(){
        binding.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onLikedOnButtonClick(idRestaurant);
            }
        });
    }

    private void conditionButtonLikedClick() {
        viewModel.getDataRestaurantClick(idRestaurant);
        viewModel.getRestaurantsLikedLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLiked) {
                    if (isLiked) {
                        binding.likeButton.setImageResource(R.drawable.star_yellow);
                    } else {
                        binding.likeButton.setImageResource(R.drawable.star);
                    }
            }
        });
    }

    private void configureOnClickPhoneButton(Result result) {
        binding.phoneButton.setOnClickListener(v -> {
            if (result.getPhoneNumber() != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + result.getPhoneNumber()));
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

    // For the recyclerView and ViewModel for the list of user picked a restaurant
    public void configureViewModelForRecyclerViewUserPickedRestaurant() {
        viewModel.getUsersPicked().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                detailsRestaurantPickedByUserAdapter.updateCallUserListPicked(users);
            }
        });
        viewModel.getUserListPicked(idRestaurant);
    }

    private void configureRecyclerView() {
        // Create adapter passing the list of articles
        this.detailsRestaurantPickedByUserAdapter = new DetailsRestaurantPickedByUserAdapter();

        // Attach the adapter to the recyclerView to populate items
        this.binding.recyclerViewDetailResto.setAdapter(this.detailsRestaurantPickedByUserAdapter);

        // Set layout manager to position the items
        this.binding.recyclerViewDetailResto.setLayoutManager(new LinearLayoutManager(this));
    }
}