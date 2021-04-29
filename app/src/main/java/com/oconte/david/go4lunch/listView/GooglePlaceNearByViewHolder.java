package com.oconte.david.go4lunch.listView;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.util.ForOpeningHours;
import com.oconte.david.go4lunch.util.ForRating;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GooglePlaceNearByViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name_resto) TextView nameResto;
    @BindView(R.id.address_type_resto) TextView adressTypeResto;
    @BindView(R.id.opening_hours_resto) TextView openingHoursResto;

    @BindView(R.id.image_resto) ImageView imageResto;

    @BindView(R.id.rating_star1) ImageView ratingStar1;
    @BindView(R.id.rating_star2) ImageView ratingStar2;
    @BindView(R.id.rating_star3) ImageView ratingStar3;



    private Resources res;

    private String formatTimeDisplay;

    public GooglePlaceNearByViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        res = itemView.getResources();
        formatTimeDisplay = res.getString(R.string.format_time_display);

    }

    public void updateWithGooglePlaceNearBy(Result result) {
        nameResto.setText(result.getName());
        adressTypeResto.setText(result.getVicinity());

        this.displayOpeningHours(result);

        Picasso.get()
                .load(getUrlPhoto(result))
                .placeholder(R.drawable.go4lunch_icon)
                .resize(60,60)
                .into(this.imageResto);

        this.displayRating(result);

    }

    public String getUrlPhoto(Result result) {
        if (result.getPhotos() != null && result.getPhotos().size() >0) {
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + result.getPhotos().get(0).getPhotoReference()+ "&key=AIzaSyCAxdxjPS79wAQ5WTz9FTtmvAfZXgIsOP8";
            return url;
        }
        return null;
    }

   @SuppressLint("StringFormatInvalid")
   private void displayOpeningHours(Result result) {
      /*  boolean openTime = result.getOpeningHours().getOpenNow();
        if (openTime) {
            openingHoursResto.setText(R.string.open_24_7);
        } else if (!openTime){
            openingHoursResto.setText(R.string.closed);
        }
       int timeOpening = result.getOpeningHours().getOpenNow();
       switch (timeOpening){
           case R.string.closed:
               openingHoursResto.setText(timeOpening);
               break;
           case R.string.closing_soon:
               openingHoursResto.setText(timeOpening);
               break;
           case R.string.no_time:
               openingHoursResto.setText(timeOpening);
               break;
           case R.string.open_24_7:
               openingHoursResto.setText(timeOpening);
               break;
           default:
               DateFormat dateFormat = new SimpleDateFormat(formatTimeDisplay);
               String timeToDisplay = dateFormat.format(ForOpeningHours.converStringInDate(timeOpening));
               openingHoursResto.setText(String.format(res.getString(R.string.open_until), timeToDisplay));
               break;


       }*/

   }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void displayRating(Result result) {
        if (result.getRating() != null) {
            int rating = ForRating.calculateRating(result.getRating());
            ratingStar1.setImageDrawable(res.getDrawable(ForRating.firstStar(rating)));
            ratingStar2.setImageDrawable(res.getDrawable(ForRating.secondStar(rating)));
            ratingStar3.setImageDrawable(res.getDrawable(ForRating.thirdStar(rating)));
        }
    }

}
