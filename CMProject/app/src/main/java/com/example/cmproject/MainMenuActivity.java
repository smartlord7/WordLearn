package com.example.cmproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmproject.fragments.BronzeChallengeFragment;
import com.example.cmproject.fragments.GoldChallengeFragment;
import com.example.cmproject.fragments.HomeFragment;
import com.example.cmproject.fragments.LoginFragment;
import com.example.cmproject.fragments.MainMenuFragment;
import com.example.cmproject.fragments.MapFragment;
import com.example.cmproject.fragments.MasterChallengeFragment;
import com.example.cmproject.fragments.ProfileFragment;
import com.example.cmproject.fragments.RegisterFragment;
import com.example.cmproject.fragments.ScoreboardFragment;
import com.example.cmproject.fragments.SilverChallengeFragment;
import com.example.cmproject.fragments.TiersFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainMenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().setTitle("WordLearn");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        LinearLayout navigationBar = findViewById(R.id.navigationBar);

        // To make the navigation bar visible again
        if (navigationBar != null) {
            navigationBar.setVisibility(View.INVISIBLE);
        }

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
        LinearLayout navigationBar = findViewById(R.id.navigationBar);

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

    public void onTierButtonClick(View view,String tier) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new MapFragment(tier));
        transaction.addToBackStack("MAP"); // Optional, to add the transaction to the back stack
        transaction.commit();
    }

    public void onProfileButtonClick(View view){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new ProfileFragment())
                .addToBackStack(null)
                .commit();
    }

    public void onScoreboardButtonClick (View view){
        Log.d("ScoreboardFragment", "Button clicked");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new ScoreboardFragment())
                .addToBackStack(null)
                .commit();
    }

    public void onEnterChallenge(View view, String tier) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(tier.equals("Bronze")) {
            transaction.replace(R.id.fragmentContainer, new BronzeChallengeFragment());
        }else if(tier.equals("Silver")){
            transaction.replace(R.id.fragmentContainer, new SilverChallengeFragment());
        }
        else if(tier.equals("Gold")){
            transaction.replace(R.id.fragmentContainer, new GoldChallengeFragment());
        } else if(tier.equals("Master")){
            transaction.replace(R.id.fragmentContainer, new MasterChallengeFragment());
        }
        transaction.addToBackStack(null); // Optional, to add the transaction to the back stack
        transaction.commit();
    }

    protected void onDestroy() {
        // Stop and release MediaPlayer when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Get the current fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        // Check if the current fragment is one of the challenge fragments
        if (currentFragment instanceof BronzeChallengeFragment ||
                currentFragment instanceof SilverChallengeFragment ||
                currentFragment instanceof GoldChallengeFragment ||
                currentFragment instanceof MasterChallengeFragment) {

            Toast.makeText(this, "Cannot go back during a challenge.", Toast.LENGTH_SHORT).show();
        } else {
            // Allow the back button press for other fragments
            super.onBackPressed();
        }
    }
}

