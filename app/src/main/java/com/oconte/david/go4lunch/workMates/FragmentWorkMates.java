package com.oconte.david.go4lunch.workMates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.databinding.FragmentWorkmatesBinding;
import com.oconte.david.go4lunch.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class FragmentWorkMates extends Fragment {

    private FragmentWorkmatesBinding binding;

    private WorkMatesAdapter workMatesAdapter;

    public static FragmentWorkMates newInstance() {
        return (new FragmentWorkMates());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Available workmates");

        this.configureViewModel();
        this.configureRecyclerView();

        return view;
    }


    public void configureViewModel() {
        WorkMatesViewModel viewModel = new ViewModelProvider(requireActivity()).get(WorkMatesViewModel.class);
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                workMatesAdapter.updateCallUserList(users);
            }
        });
        viewModel.getUserList();

    }


    private void configureRecyclerView() {
        // Create adapter passing the list of articles
        this.workMatesAdapter = new WorkMatesAdapter();

        // Attach the adapter to the recyclerView to populate items
        this.binding.fragmentMainRecyclerViewWorkMates.setAdapter(this.workMatesAdapter);

        // Set layout manager to position the items
        this.binding.fragmentMainRecyclerViewWorkMates.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}
