package com.example.cmproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmproject.fragments.HomeFragment;
import com.example.cmproject.fragments.LoginFragment;
import com.example.cmproject.fragments.MainMenuFragment;
import com.example.cmproject.fragments.MapFragment;
import com.example.cmproject.fragments.RegisterFragment;
import com.example.cmproject.fragments.TiersFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainMenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().setTitle("WordLearn");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));


        LinearLayout navigationBar = findViewById(R.id.navigationBar); // Use view.findViewById if in a fragment

        // To make the navigation bar visible again
        if (navigationBar != null) {
            navigationBar.setVisibility(View.INVISIBLE);
        }
        findViewById(R.id.btnRankedPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRankedPlayButtonClick(v);
            }
        });

        findViewById(R.id.btnScoreboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onScoreboardButtonClick(v);
            }
        });


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new MainMenuFragment())
                .addToBackStack(null)  // Optional: Adds the transaction to the back stack
                .commit();
       /* InputStream silverInputStream = getResources().openRawResource(R.raw.silver_file);
        FirebaseUploader firebaseUploader = new FirebaseUploader("silver", silverInputStream);
        InputStream goldInputStream = getResources().openRawResource(R.raw.gold_file);
        FirebaseUploader firebaseUploader2 = new FirebaseUploader("gold",goldInputStream);
        InputStream masterInputStream = getResources().openRawResource(R.raw.master_file);
        FirebaseUploader firebaseUploader3 = new FirebaseUploader("master", masterInputStream);
        firebaseUploader.uploadToFirebase();
        firebaseUploader2.uploadToFirebase();
        firebaseUploader3.uploadToFirebase();*/
    }


    public void onLoginButtonClick(View view) {
        // Handle login button click
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new LoginFragment())
                .addToBackStack(null)  // Optional: Adds the transaction to the back stack
                .commit();
    }

    public void onLoginSuccessful(View view) {
        LinearLayout navigationBar = findViewById(R.id.navigationBar); // Use view.findViewById if in a fragment

        // To make the navigation bar visible again
        if (navigationBar != null) {
            navigationBar.setVisibility(View.VISIBLE);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment())
                .addToBackStack(null)  // Optional: Adds the transaction to the back stack
                .commit();
    }


    public void onRegisterButtonClick(View view) {
        // Handle register button click
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new RegisterFragment())
                .addToBackStack(null)  // Optional: Adds the transaction to the back stack
                .commit();
    }

    public void onRankedPlayButtonClick(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new TiersFragment())
                .addToBackStack(null)  // Optional: Adds the transaction to the back stack
                .commit();

    }

    public void onTierButtonClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new MapFragment());
        transaction.addToBackStack(null); // Optional, to add the transaction to the back stack
        transaction.commit();
    }

}

