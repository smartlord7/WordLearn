package com.example.cmproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;

public class TierDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tier_details);

        // Retrieve the selected tier from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String selectedTier = intent.getStringExtra("selectedTier");
            setTitle(selectedTier + " Details");
            // Set up the grid based on the selected tier
            setupGrid(selectedTier);
        }
    }

    private void setupGrid(String selectedTier) {
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Loop to add cells dynamically
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                View cellView = getLayoutInflater().inflate(R.layout.cell_layout, null);
                gridLayout.addView(cellView);
            }
        }
    }
}
