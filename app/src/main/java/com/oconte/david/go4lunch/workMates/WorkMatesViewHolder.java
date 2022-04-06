package com.oconte.david.go4lunch.workMates;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.databinding.WorkmatesItemRecyclerViewBinding;
import com.oconte.david.go4lunch.models.User;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class WorkMatesViewHolder extends RecyclerView.ViewHolder {

    private final WorkmatesItemRecyclerViewBinding binding;
    private final Resources res;

    public WorkMatesViewHolder(@NonNull @NotNull WorkmatesItemRecyclerViewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

        res = itemView.getResources();
    }

    public void updateWithUser(User user) {
        binding.recyclerViewText.setText(user.getUsername());

        Picasso.get()
                .load(user.getUrlPicture())
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.go4lunch_icon)
                .into(this.binding.recyclerViewImageUser);
    }
}
