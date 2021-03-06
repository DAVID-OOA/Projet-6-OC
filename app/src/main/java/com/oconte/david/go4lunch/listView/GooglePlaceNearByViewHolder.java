package com.oconte.david.go4lunch.listView;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.BuildConfig;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.OpeningHours;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.util.ForOpeningHours;
import com.oconte.david.go4lunch.util.ForRating;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GooglePlaceNearByViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name_resto) TextView nameResto;
    @BindView(R.id.address_type_resto) TextView adressTypeResto;
    @BindView(R.id.opening_hours_resto) TextView openingHoursResto;

    @BindView(R.id.image_resto) ImageView imageResto;

    @BindView(R.id.rating_star1) ImageView ratingStar1;

    private Resources res;



    public GooglePlaceNearByViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }

    public void updateWithGooglePlaceNearBy(Result result) {
        nameResto.setText(result.getName());
        adressTypeResto.setText(result.getVicinity());

        //openingHoursResto.setText(result.getOpeningHours().getOpenNow());

        Picasso.get()
                .load(getUrlPhoto(result))
                .placeholder(R.drawable.go4lunch_icon)
                .resize(60,60)
                .into(this.imageResto);



    }




    public String getUrlPhoto(Result result) {
        if (result.getPhotos() != null && result.getPhotos().size() >0) {
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + result.getPhotos().get(0).getPhotoReference()+ "&key=AIzaSyCAxdxjPS79wAQ5WTz9FTtmvAfZXgIsOP8";
            return url;
        }

        return null;
    }

    private void displayOpeningHours(Result result) {

    }

    /*private void forRating(Result result) {
        int rating = result.getRating();
        ratingStar1.setImageDrawable(res.getDrawable(ForRating.firstStar(rating)));
    }*/

}
