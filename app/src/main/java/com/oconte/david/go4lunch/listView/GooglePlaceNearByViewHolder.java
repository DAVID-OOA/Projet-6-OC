package com.oconte.david.go4lunch.listView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.models.Result;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GooglePlaceNearByViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name_resto) TextView textView;


    public GooglePlaceNearByViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updateWithGooglePlaceNearBy(Result result) {
        //String title = result.getName();
        //this.textView.setText(title);
        textView.setText(result.getName());
    }
}
