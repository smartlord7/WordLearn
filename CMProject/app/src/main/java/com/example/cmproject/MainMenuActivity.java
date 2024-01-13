package com.example.cmproject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cmproject.fragments.ChallengeFragment;
import com.example.cmproject.fragments.ScoreFragment;

import java.io.InputStream;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu); // Set your layout XML file here

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        InputStream bronzeInputStream = getResources().openRawResource(R.raw.bronze_file);
        InputStream silverInputStream = getResources().openRawResource(R.raw.silver_file);
        InputStream goldInputStream = getResources().openRawResource(R.raw.gold_file);
        InputStream masterInputStream = getResources().openRawResource(R.raw.master_file);

        dbHelper.uploadWordsFromFile("bronze", bronzeInputStream);
        dbHelper.uploadWordsFromFile("silver", silverInputStream);
        dbHelper.uploadWordsFromFile("gold", goldInputStream);
        dbHelper.uploadWordsFromFile("master", masterInputStream);

        dbHelper.closeDatabase();
    }

    public void onLoginButtonClick(View view) {
        // Handle login button click
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onRegisterButtonClick(View view) {
        // Handle register button click
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onRankedButtonClick(View view) {
        // Handle ranked button click
        Intent intent = new Intent(this, RankedActivity.class);
        startActivity(intent);
    }

    public void onScoreboardButtonClick(View view) {
        // Handle scoreboard button click
        Intent intent = new Intent(this, ScoreboardActivity.class);
        startActivity(intent);
    }

}
