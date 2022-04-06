package com.oconte.david.go4lunch.restodetails;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.databinding.RestoUserItemRecyclerViewBinding;
import com.oconte.david.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;

public class DetailsRestaurantPickedByUserAdapter extends RecyclerView.Adapter<DetailsRestaurantPickedByUserViewHolder> {

    private List<User> usersPicked = new ArrayList<>();

    @NonNull
    @Override
    public DetailsRestaurantPickedByUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsRestaurantPickedByUserViewHolder(RestoUserItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsRestaurantPickedByUserViewHolder viewHolder, int position) {
        viewHolder.updateWithUserPicked(usersPicked.get(position));

    }

    @Override
    public int getItemCount() {
        return this.usersPicked.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCallUserListPicked(List<User> userListPicked) {
        this.usersPicked = userListPicked;
        this.notifyDataSetChanged();
    }
}
