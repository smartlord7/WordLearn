package com.example.cmproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);
    }

    public void onRankedButtonClick(View view) {
        // Handle Ranked Play button click
        Intent intent = new Intent(this, TiersActivity.class);
        startActivity(intent);
    }

    public void onScoreboardButtonClick(View view) {
        // Handle Scoreboard button click
        Intent intent = new Intent(this, ScoreboardActivity.class);
        startActivity(intent);
    }
}

