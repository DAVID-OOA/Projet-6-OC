package com.oconte.david.go4lunch.listView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.Result;

import static android.media.CamcorderProfile.get;

public class GooglePlaceNearByAdapter extends RecyclerView.Adapter<GooglePlaceNearByViewHolder> {

    private ApiNearByResponse apiNearByResponse = new ApiNearByResponse();


    @NonNull
    @Override
    public GooglePlaceNearByViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.resto_item_recycler_view, parent,false);

        return new GooglePlaceNearByViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GooglePlaceNearByViewHolder viewHolder, int position) {
        viewHolder.updateWithGooglePlaceNearBy(this.apiNearByResponse.results.get(position));



    }

    @Override
    public int getItemCount() {
        return this.apiNearByResponse.results.size();
    }

    public void updateCallRetrofitGoogleNearBy(ApiNearByResponse apiNearByResponses) {
        this.apiNearByResponse = apiNearByResponses;
        this.notifyDataSetChanged();
    }
}
