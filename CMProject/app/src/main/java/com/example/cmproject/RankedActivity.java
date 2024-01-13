package com.example.cmproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class RankedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranked);

        // Your existing code for initializing the RankedActivity
    }

    public void onStartChallengeRankedButtonClick(View view) {
        // Handle start challenge button click
        Intent intent = new Intent(this, ChallengeActivity.class); // Replace with your ChallengeActivity
        startActivity(intent);
    }
}
