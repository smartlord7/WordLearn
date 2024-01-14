package com.example.cmproject.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cmproject.R;
import com.example.cmproject.views.ChallengeMapViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;


public class ScoreboardFragment extends Fragment {

    private TextView bronzeTextView, silverTextView, goldTextView, masterTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scoreboard, container, false);

        // Initialize UI components
        bronzeTextView = view.findViewById(R.id.bronzeScoreTextView);
        silverTextView = view.findViewById(R.id.silverScoreTextView);
        goldTextView = view.findViewById(R.id.goldScoreTextView);
        masterTextView = view.findViewById(R.id.masterScoreTextView);

        // Fetch and display scoreboard data
        loadScoreboardData();

        return view;
    }

    private void loadScoreboardData() {
        // Fetch and display top 3 players for each tier
        fetchAndDisplayTopPlayers("Bronze", bronzeTextView);
        fetchAndDisplayTopPlayers("Silver", silverTextView);
        fetchAndDisplayTopPlayers("Gold", goldTextView);
        fetchAndDisplayTopPlayers("Master", masterTextView);
    }

    private void fetchAndDisplayTopPlayers(String tier, TextView textView) {
        Executors.newSingleThreadExecutor().execute(() -> {
            DatabaseReference markersReference = FirebaseDatabase.getInstance().getReference("markers");

            markersReference.orderByChild("tier").equalTo(tier).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Double> userScores = new HashMap<>();
                    Map<String, Integer> userMarkerCount = new HashMap<>();

                    // Calculate total scores and marker counts for each user in the specified tier
                    for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                        String ownerEmail = markerSnapshot.child("owner").getValue(String.class);
                        double score = markerSnapshot.child("score").getValue(Double.class);

                        // Sum up the scores for each user
                        if (userScores.containsKey(ownerEmail)) {
                            userScores.put(ownerEmail, userScores.get(ownerEmail) + score);
                        } else {
                            userScores.put(ownerEmail, score);
                        }

                        // Count the number of markers for each user
                        userMarkerCount.put(ownerEmail, userMarkerCount.getOrDefault(ownerEmail, 0) + 1);
                    }

                    // Convert the Map to a List of Map.Entry for sorting
                    List<Map.Entry<String, Double>> sortedUserScores = new ArrayList<>(userScores.entrySet());
                    Collections.sort(sortedUserScores, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                    // Display the top 3 players for the tier along with marker counts
                    StringBuilder displayText = new StringBuilder();
                    int count = 0;
                    for (Map.Entry<String, Double> entry : sortedUserScores) {
                        String userEmail = entry.getKey();
                        double totalScore = entry.getValue();
                        int markerCount = userMarkerCount.get(userEmail);

                        displayText.append(String.format(Locale.getDefault(), "\n%s: %.2f (Markers: %d)", userEmail, totalScore, markerCount));
                        count++;

                        if (count >= 3) {
                            break; // Display only the top 3 players
                        }
                    }

                    // Update the textView accordingly
                    requireActivity().runOnUiThread(() -> {
                        textView.setText(displayText.toString());
                        textView.setGravity(Gravity.CENTER); // Center the text horizontally
                        textView.setGravity(Gravity.CENTER_VERTICAL); // Center the text vertically
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors or log them
                }
            });
        });
    }

}


