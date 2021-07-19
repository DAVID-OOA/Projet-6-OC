package com.oconte.david.go4lunch.workMates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.databinding.FragmentWorkmatesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FragmentWorkMates extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static FragmentWorkMates newInstance() {
        return (new FragmentWorkMates());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.oconte.david.go4lunch.databinding.FragmentWorkmatesBinding binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Available workmates");


        return view;
    }




    public void getDocument() {
        DocumentReference docRef = db.collection("workMates").document("USER");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

            }
        });
    }

}
