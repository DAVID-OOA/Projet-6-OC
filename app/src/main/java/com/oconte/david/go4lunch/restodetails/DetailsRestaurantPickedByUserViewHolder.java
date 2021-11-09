package com.oconte.david.go4lunch.restodetails;



import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.RestoItemRecyclerViewBinding;
import com.oconte.david.go4lunch.databinding.RestoUserItemRecyclerViewBinding;
import com.oconte.david.go4lunch.models.User;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DetailsRestaurantPickedByUserViewHolder extends RecyclerView.ViewHolder {

    private final RestoUserItemRecyclerViewBinding binding;
    private final Resources res;

    public DetailsRestaurantPickedByUserViewHolder(@NonNull RestoUserItemRecyclerViewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

        res = itemView.getResources();
    }

    public void updateWithUserPicked(User user) {
        binding.recyclerViewText.setText(user.getUsername());

        if (user.getUrlPicture() != null) {
            Picasso.get()
                    .load(user.getUrlPicture())
                    .transform(new CropCircleTransformation())
                    .placeholder(R.drawable.go4lunch_icon)
                    .into(this.binding.recyclerViewImageUser);
        }

    }

}
