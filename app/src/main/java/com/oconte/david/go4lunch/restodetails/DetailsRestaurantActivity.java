package com.oconte.david.go4lunch.restodetails;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.SetOptions;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.DetailViewRestoBinding;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.util.ForRating;
import com.oconte.david.go4lunch.workMates.UserRepository;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailsRestaurantActivity extends AppCompatActivity {

    private DetailViewRestoBinding binding;

    public DetailsRestaurantViewModel viewModel;


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("restaurants");

    Result result;

    String idRestaurant;

    Boolean isLiked = false;

    private Resources res;
    boolean buttonOn;
    private final UserRepository userRepository;
    FirebaseUser user;

    public DetailsRestaurantActivity() {
        userRepository = UserRepository.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailViewRestoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        res = binding.detailRestaurantRootView.getResources();

        Intent intent = getIntent();

        result = (Result)intent.getSerializableExtra("result");

        this.configureViewDetailsRestaurant(result);

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
            user = userRepository.getCurrentUser();

            this.displayRating(result);

            this.configureOnClickPhoneButton(result);

            this.configureOnClickWebSite(result);

            this.configureOnClicFloatingButton();

            this.configureOnClickLikeButton();

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

    private void configureOnClicFloatingButton() {
        FloatingActionButton button = binding.pickRestaurantButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonOn) {
                    buttonOn = true;
                    binding.pickRestaurantButton.setImageResource(R.drawable.floatingbuttonoff);
                    //binding.pickRestaurantButton.setImageResource(R.drawable.star_yellow);
                } else {
                    buttonOn = false;
                    binding.pickRestaurantButton.setImageResource(R.drawable.floatingbutton);
                    //binding.pickRestaurantButton.setImageResource(R.drawable.star);
                }
            }
        });
    }


    private void conditionButtonLikedClick() {
        collectionReference.document(idRestaurant).collection("liked").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                //si document n'est pas vide le boutton prend la couleur verte.
                if (Objects.requireNonNull(snapshot).exists()) {
                    isLiked = true;
                    binding.likeButton.setImageResource(R.drawable.star_yellow);
                    Toast.makeText(DetailsRestaurantActivity.this,"switch on yellow", Toast.LENGTH_LONG).show();
                    Log.d("TAG","switch on yellow");

                } else {
                    isLiked = false;
                    binding.likeButton.setImageResource(R.drawable.star);
                    Toast.makeText(DetailsRestaurantActivity.this,"switch on black", Toast.LENGTH_LONG).show();
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
                if (!buttonOn) {
                    buttonOn = true;
                    if (userRepository.isCurrentUserLogged()) {

                        String idUser = Objects.requireNonNull(user).getUid();
                        String userName = user.getDisplayName();
                        String urlPhoto = String.valueOf(user.getPhotoUrl());

                        Restaurant restaurant = new Restaurant(urlPhoto,userName,idUser, idRestaurant);

                        collectionReference.document(idRestaurant)
                                .collection("liked").document(idUser).set(restaurant, SetOptions.merge());

                    }
                } else {
                    buttonOn = false;
                    Log.d("TAG","switch on black!!!!!!!!!!!!!!!!!!!!!!!!!");
                    collectionReference.document(idRestaurant).collection("liked").document(user.getUid()).delete();
                    Toast.makeText(getApplicationContext(),"test for deleted",Toast.LENGTH_LONG).show();
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