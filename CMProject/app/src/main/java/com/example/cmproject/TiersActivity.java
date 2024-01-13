package com.example.cmproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class TiersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiers);
    }

    public void onTierButtonClick(View view) {
        String tier = "";

        if (view.getId() == R.id.btnBronze) {
            tier = "Bronze";
        } else if (view.getId() == R.id.btnSilver) {
            tier = "Silver";
        } else if (view.getId() == R.id.btnGold) {
            tier = "Gold";
        } else if (view.getId() == R.id.btnMaster) {
            tier = "Master";
        }

        // Pass the selected tier to the next activity (you can customize this part)
        Intent intent = new Intent(this, TierDetailsActivity.class);
        intent.putExtra("selectedTier", tier);
        startActivity(intent);
    }

}
