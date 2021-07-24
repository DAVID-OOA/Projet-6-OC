package com.oconte.david.go4lunch.workMates;

import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.databinding.WorkmatesItemRecyclerViewBinding;
import com.oconte.david.go4lunch.models.User;

import org.jetbrains.annotations.NotNull;

public class WorkMatesViewHolder extends RecyclerView.ViewHolder {


    private WorkmatesItemRecyclerViewBinding binding;
    private final Resources res;


    public WorkMatesViewHolder(@NonNull @NotNull WorkmatesItemRecyclerViewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

        res = itemView.getResources();
    }

    public void updateWithUser(User user) {
        binding.recyclerViewText.setText(user.getUsername());
    }
}
