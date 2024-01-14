package com.example.cmproject;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ZoomableGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomable_grid);

        ZoomableTileGridView zoomableTileGridView = findViewById(R.id.zoomableTileGridView);

        // Set a callback for cell clicks
        zoomableTileGridView.setOnCellClickListener(new ZoomableTileGridView.OnCellClickListener() {
            @Override
            public void onCellClick(int row, int col) {
                // Handle the cell click event
                // You can perform any action you need when a cell is clicked
            }
        });
    }
}