package com.oconte.david.go4lunch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_auth);
            ButterKnife.bind(this);

        }
}
