package com.oconte.david.go4lunch.workMates;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oconte.david.go4lunch.databinding.WorkmatesItemRecyclerViewBinding;
import com.oconte.david.go4lunch.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorkMatesAdapter extends RecyclerView.Adapter<WorkMatesViewHolder> {

    private List<User> users = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public WorkMatesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new WorkMatesViewHolder(WorkmatesItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WorkMatesViewHolder viewHolder, int position) {
        viewHolder.updateWithUser(users.get(position));
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCallUserList(List<User> userList) {
        this.users = userList;
        this.notifyDataSetChanged();
    }
}
