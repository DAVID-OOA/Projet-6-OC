package com.oconte.david.go4lunch.listView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.oconte.david.go4lunch.MainActivity;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.RestoItemRecyclerViewBinding;
import com.oconte.david.go4lunch.mapView.FragmentMapView;
import com.oconte.david.go4lunch.models.OpeningHours;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.util.ForOpeningHours;
import com.oconte.david.go4lunch.util.ForRating;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.test.InstrumentationRegistry.getContext;

public class GooglePlaceNearByViewHolder extends RecyclerView.ViewHolder {

    private final RestoItemRecyclerViewBinding binding;
    private final Resources res;
    private String formatTimeDisplay;


    public GooglePlaceNearByViewHolder(@NonNull RestoItemRecyclerViewBinding binding) {
        super(binding.getRoot());
         this.binding = binding;

        res = itemView.getResources();
        formatTimeDisplay = res.getString(R.string.format_time_display);

    }


    @SuppressLint("SetTextI18n")
    public void updateWithGooglePlaceNearBy(Result result) {
        binding.nameResto.setText(result.getName());
        binding.addressTypeResto.setText(result.getVicinity());

        this.displayOpeningHours(result);

        Picasso.get()
                .load(getUrlPhoto(result))
                .placeholder(R.drawable.go4lunch_icon)
                .resize(60,60)
                .into(this.binding.imageResto);

        this.displayRating(result);

        Double distance = result.getGeometry().getDistance();
        String writeDistance = distance + " m";
        binding.distanceResto.setText(writeDistance);


    }

    public String getUrlPhoto(Result result) {
        if (result.getPhotos() != null && result.getPhotos().size() > 0) {
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + result.getPhotos().get(0).getPhotoReference()+ "&key=AIzaSyCAxdxjPS79wAQ5WTz9FTtmvAfZXgIsOP8";
            return url;
        }
        return null;
    }


   private void displayOpeningHours(Result result) {
      OpeningHours openTime = result.getOpeningHours();
      if (openTime == null) {
          binding.openingHoursResto.setText(R.string.open_24_7);
      } else {
          binding.openingHoursResto.setText(R.string.closed);
      }
   }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void displayRating(Result result) {
        if (result.getRating() != null) {
            int rating = ForRating.calculateRating(result.getRating());
            binding.ratingStar1.setImageDrawable(res.getDrawable(ForRating.firstStar(rating)));
            binding.ratingStar2.setImageDrawable(res.getDrawable(ForRating.secondStar(rating)));
            binding.ratingStar3.setImageDrawable(res.getDrawable(ForRating.thirdStar(rating)));
        }
    }


}