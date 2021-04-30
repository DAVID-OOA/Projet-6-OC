package com.oconte.david.go4lunch;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.oconte.david.go4lunch.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    //For Design
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.configureToolbar();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *  - Configure the Toolbar
     */
    protected void configureToolbar() {
        setSupportActionBar(binding.layoutToolbar.toolbar);
        getSupportActionBar().setTitle("I'm Hungry !");

    }
}
