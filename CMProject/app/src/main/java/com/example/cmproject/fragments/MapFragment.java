package com.example.cmproject.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cmproject.MainMenuActivity;
import com.example.cmproject.R;
import com.example.cmproject.util.MarkerData;
import com.example.cmproject.views.ChallengeMapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LatLng currentPoint;

    private DatabaseReference markersReference;

    private String tier;

    private ChallengeMapViewModel sharedViewModel;

    public MapFragment(String tier) {
        this.tier = tier;
        markersReference = FirebaseDatabase.getInstance().getReference("markers");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeMap(); // Call the method to initialize the map

        sharedViewModel.getData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String yourVariable) {
                if(currentPoint != null) {
                    Log.d("MapFragment", "onChanged: " + yourVariable);
                    double score = Double.parseDouble(Objects.requireNonNull(sharedViewModel.getData().getValue()));
                    double latitude = currentPoint.latitude;
                    double longitude = currentPoint.longitude;
                    addMarkerToDatabase(latitude, longitude, score);
                }
            }
        });
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle permissions if needed
            return;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save your fragment's state to the bundle
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(ChallengeMapViewModel.class);

        if (savedInstanceState != null) {
            // Restore your fragment's state from the savedInstanceState
            googleMap = (GoogleMap) savedInstanceState.getSerializable("googleMap");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add your map-related logic here
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        checkLocationPermission();

        // Set maximum and minimum zoom levels
        googleMap.setMaxZoomPreference(5.0f); // Adjust the value according to your requirement
        googleMap.setMinZoomPreference(3); // Adjust the value according to your requirement

        loadMarkersForTier(tier);


        // Set an initial marker (for example, the center of the map)
        // Inside your onMapReady or wherever you want to add the circle
        LatLng initialLatLng = new LatLng(googleMap.getCameraPosition().target.latitude,
                googleMap.getCameraPosition().target.longitude);

        // Remove the marker creation and use CircleOptions to create a circle
        CircleOptions circleOptions = new CircleOptions()
                .center(initialLatLng)
                .radius(100) // Set the radius in meters as needed
                .strokeWidth(2) // Set the stroke width
                .strokeColor(Color.BLUE) // Set the stroke color
                .fillColor(Color.argb(0, 0, 0, 255)); // Set the fill color with transparency

        // Add the circle to the map
        Circle currentCircle = googleMap.addCircle(circleOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialLatLng));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Handle map click event
                // Add a marker at the clicked location
                showConfirmationDialog(latLng.latitude, latLng.longitude, true);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LatLng markerLatLng = marker.getPosition();
                double latitude = markerLatLng.latitude;
                double longitude = markerLatLng.longitude;

                // You can also update other UI elements or perform additional actions as needed
                Toast.makeText(requireContext(), "Marker selected at " + String.format("Latitude: %.3f, Longitude: %.3f", latitude, longitude), Toast.LENGTH_SHORT).show();

                // Show a confirmation dialog
                showConfirmationDialog(latitude, longitude, false);

                // Return false to allow the default behavior (showing the info window)
                return false;
            }
        });

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // Handle marker drag start if needed
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // Handle marker drag if needed
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // Handle marker drag end if needed
            }
        });

        int zoomInt = (int) googleMap.getCameraPosition().zoom;
        final int[] lastZoom = {-1};
        googleMap.setOnCameraMoveListener(() -> {
            int zoomInt1 = (int) googleMap.getCameraPosition().zoom;
            if (lastZoom[0] == -1) {
                lastZoom[0] = zoomInt1;
                return;
            }
            // Access views using getView()
            View rootView = getView();
            if (zoomInt1 <= 3) {
                rootView.findViewById(R.id.grid_small).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.grid_medium).setVisibility(View.GONE);
                rootView.findViewById(R.id.grid_large).setVisibility(View.GONE);
            } else if (zoomInt1 <= 4) {
                rootView.findViewById(R.id.grid_small).setVisibility(View.GONE);
                rootView.findViewById(R.id.grid_medium).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.grid_large).setVisibility(View.GONE);
            } else {
                rootView.findViewById(R.id.grid_small).setVisibility(View.GONE);
                rootView.findViewById(R.id.grid_medium).setVisibility(View.GONE);
                rootView.findViewById(R.id.grid_large).setVisibility(View.VISIBLE);
            }

            lastZoom[0] = zoomInt1;
        });
    }

    private void loadMarkersForTier(String selectedTier) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Retrieve markers from the Firebase database based on the selected tier
            markersReference.orderByChild("tier").equalTo(selectedTier)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MarkerData markerData = snapshot.getValue(MarkerData.class);

                                if (markerData != null) {
                                    // Extract marker information
                                    double latitude = markerData.getLatitude();
                                    double longitude = markerData.getLongitude();
                                    double score = markerData.getScore();

                                    // Update the UI on the main thread
                                    requireActivity().runOnUiThread(() -> {
                                        // Add marker to the map
                                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .title("Score: " + score));

                                        // Customize the marker appearance as needed
                                        // For example, you can use different icons for different tiers
                                        if (selectedTier.equals("Bronze")) {
                                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                        } else if (selectedTier.equals("Silver")) {
                                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                        } else if (selectedTier.equals("Gold")) {
                                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                        } else if (selectedTier.equals("Master")) {
                                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors or log them
                        }
                    });
        });
    }

    private void showConfirmationDialog(double latitude, double longitude, boolean isNew) {
        @SuppressLint("DefaultLocale") String locationInfo = String.format("Latitude: %.3f, Longitude: %.3f", latitude, longitude);
        if (isNew) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Enter Challenge")
                    .setMessage("Do you want to enter a challenge at this location?\n\n" + locationInfo)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // User clicked "Yes," handle the action (e.g., navigate to the challenge screen)
                        this.currentPoint = new LatLng(latitude, longitude);
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).draggable(false));
                        enterChallenge(latitude, longitude);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // User clicked "No," dismiss the dialog
                        dialog.dismiss();
                    })
                    .show();
        } else {
            // Not a new marker, fetch owner and score from the database
            markersReference.orderByChild("latitude").equalTo(latitude)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MarkerData markerData = snapshot.getValue(MarkerData.class);

                                if (markerData != null) {
                                    // Extract marker information
                                    String email = markerData.getOwner();
                                    double score = markerData.getScore();
                                    showChallengeConfirmationDialog(email, latitude, longitude, score);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors or log them
                        }
                    });
        }
    }

    @SuppressLint("DefaultLocale")
    private void showChallengeConfirmationDialog(String ownerEmail, double latitude, double longitude, double score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Challenge Confirmation")
                .setMessage(String.format("Do you want to challenge '%s' with a score of %.2f at latitude %.3f and longitude %.3f?", ownerEmail, score, latitude, longitude))
                .setPositiveButton("Yes", (dialog, which) -> {
                    // User clicked "Yes," handle the action (e.g., navigate to the challenge screen)
                    this.currentPoint = new LatLng(latitude, longitude);
                    enterChallenge(latitude, longitude);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // User clicked "No," dismiss the dialog
                    dialog.dismiss();
                })
                .show();
    }


    private void addMarkerToDatabase(double latitude, double longitude, double score) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            // Check if a marker with the same latitude and longitude already exists
            Query markerQuery = markersReference.orderByChild("latitude").equalTo(latitude);
            markerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MarkerData existingMarker = snapshot.getValue(MarkerData.class);

                        // Check if the score is higher
                        if (existingMarker != null && score > existingMarker.getScore()) {
                            // Update the existing marker with the new owner and score
                            markersReference.child(snapshot.getKey()).child("owner").setValue(userEmail);
                            markersReference.child(snapshot.getKey()).child("score").setValue(score);
                            return;
                        } else if (existingMarker != null && score < existingMarker.getScore()) {
                            return;
                        }
                    }
                    // If no existing marker found or the new score is not higher, create a new marker
                    String markerId = UUID.randomUUID().toString();
                    MarkerData markerData = new MarkerData(markerId,tier, latitude, longitude, score, userEmail);
                    markersReference.child(markerId).setValue(markerData);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors or log them
                }
            });
        }
    }


    private void enterChallenge(double latitude, double longitude) {
        // Perform actions to handle entering the challenge at the specified coordinates
        // For example, you can navigate to a new activity or fragment for the challenge
        // Make sure to pass the latitude and longitude to the next screen if needed
        Toast.makeText(requireContext(), "Entering challenge at " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
        // Add your navigation logic here

        ((MainMenuActivity) (getActivity())).onEnterChallenge(getView(), tier);
    }
}
