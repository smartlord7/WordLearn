package com.example.cmproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cmproject.MainMenuActivity;
import com.example.cmproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private TextView welcomeTextView,emailTextView, markersTextView, bronzeMarkersTextView, silverMarkersTextView,goldMarkersTextView,masterMarkersTextView;
    private DatabaseReference markersReference;

    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI components
        welcomeTextView = view.findViewById(R.id.welcomeTextView);
        markersTextView = view.findViewById(R.id.markersTextView);
        bronzeMarkersTextView = view.findViewById(R.id.bronzeMarkersTextView);
        silverMarkersTextView = view.findViewById(R.id.silverMarkersTextView);
        goldMarkersTextView = view.findViewById(R.id.goldMarkersTextView);
        masterMarkersTextView = view.findViewById(R.id.masterMarkersTextView);

        logoutButton = view.findViewById(R.id.logoutButton);

        // Set click listener for logout button
        logoutButton.setOnClickListener(v -> logout());

        // Initialize Firebase
        markersReference = FirebaseDatabase.getInstance().getReference("markers");

        // Retrieve user information from Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Set username and email to TextViews
            welcomeTextView.setText(String.format("Welcome %s", user.getEmail()));

            // Load markers data
            loadMarkersData(user.getEmail());
        }

        return view;
    }

    private void loadMarkersData(String userEmail) {
        Executors.newSingleThreadExecutor().execute(() -> {
            markersReference.orderByChild("owner").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                        // Extract marker data
                        String tier = markerSnapshot.child("tier").getValue(String.class);
                        double latitude = markerSnapshot.child("latitude").getValue(Double.class);
                        double longitude = markerSnapshot.child("longitude").getValue(Double.class);
                        double score = markerSnapshot.child("score").getValue(Double.class);

                        // Display markers data for the specified tier on the UI thread
                        requireActivity().runOnUiThread(() -> displayMarkersData(tier, latitude, longitude, score));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        });
    }


    private void displayMarkersData(String tier, double latitude, double longitude, double score) {
        // Display markers data for the specified tier
        switch (tier) {
            case "Bronze":
                bronzeMarkersTextView.append(String.format(Locale.getDefault(), "\nLAT: %.2f     ||     LONG: %.2f     ||     SCORE: %.2f", latitude, longitude, score));
                break;
            case "Silver":
                silverMarkersTextView.append(String.format(Locale.getDefault(), "\nLAT: %.2f     ||     LONG: %.2f     ||     SCORE: %.2f", latitude, longitude, score));
                break;
            case "Gold":
                goldMarkersTextView.append(String.format(Locale.getDefault(), "\nLAT: %.2f     ||     LONG: %.2f     ||     SCORE: %.2f", latitude, longitude, score));
                break;
            case "Master":
                masterMarkersTextView.append(String.format(Locale.getDefault(), "\nLAT: %.2f     ||     LONG: %.2f     ||     SCORE: %.2f", latitude, longitude, score));
                break;
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();

        // Create an Intent to navigate back to MainMenuActivity
        Intent intent = new Intent(requireActivity(), MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
