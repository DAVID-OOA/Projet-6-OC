package com.oconte.david.go4lunch.workMates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.databinding.FragmentWorkmatesBinding;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.repositories.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class FragmentWorkMates extends Fragment {

    private FragmentWorkmatesBinding binding;

    public WorkMatesViewModel viewModel;
    private WorkMatesAdapter workMatesAdapter;

    //UserRepository userRepository;

    public static FragmentWorkMates newInstance() {
        return (new FragmentWorkMates());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        this.configureViewDetailsRestaurantFactory();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Available workmates");

        this.configureViewModel();
        this.configureRecyclerView();

        return view;
    }

    public void configureViewDetailsRestaurantFactory() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(FragmentWorkMates.this, viewModelFactory);
        viewModel = viewModelProvider.get(WorkMatesViewModel.class);
    }

    public void configureViewModel() {
        viewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
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
