package com.oconte.david.go4lunch.listView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.databinding.RestoItemRecyclerViewBinding;
import com.oconte.david.go4lunch.models.Result;

import java.util.ArrayList;
import java.util.List;

public class GooglePlaceNearByAdapter extends RecyclerView.Adapter<GooglePlaceNearByViewHolder> {

    private List<Result> apiNearByResponse = new ArrayList<>();

    @NonNull
    @Override
    public GooglePlaceNearByViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GooglePlaceNearByViewHolder(RestoItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GooglePlaceNearByViewHolder viewHolder, int position) {
        viewHolder.updateWithGooglePlaceNearBy(this.apiNearByResponse.get(position));
    }

    @Override
    public int getItemCount() {
        return this.apiNearByResponse.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCallRetrofitGoogleNearBy(List<Result> resultList) {
        this.apiNearByResponse = resultList;
        this.notifyDataSetChanged();
    }
}
