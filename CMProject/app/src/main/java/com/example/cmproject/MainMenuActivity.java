package com.example.cmproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmproject.fragments.LoginFragment;
import com.example.cmproject.fragments.MainMenuFragment;
import com.example.cmproject.fragments.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainMenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
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


    public void onRegisterButtonClick(View view) {
        // Handle register button click
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new RegisterFragment())
                .addToBackStack(null)  // Optional: Adds the transaction to the back stack
                .commit();
    }

}

