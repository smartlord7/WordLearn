package com.example.cmproject.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.cmproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Marker currentMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeMap(); // Call the method to initialize the map
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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add your map-related logic here
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        checkLocationPermission();

        // Set maximum and minimum zoom levels
        googleMap.setMaxZoomPreference(5.0f); // Adjust the value according to your requirement
        googleMap.setMinZoomPreference(3); // Adjust the value according to your requirement

        // Set an initial marker (for example, the center of the map)
        LatLng initialLatLng = new LatLng(googleMap.getCameraPosition().target.latitude,
                googleMap.getCameraPosition().target.longitude);
        currentMarker = googleMap.addMarker(new MarkerOptions().position(initialLatLng).draggable(true));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialLatLng));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Handle map click event
                // Add a marker at the clicked location
                Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));

                // You can also update other UI elements or perform additional actions as needed
                Toast.makeText(requireContext(), "Marker placed at " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LatLng markerLatLng = marker.getPosition();
                double latitude = markerLatLng.latitude;
                double longitude = markerLatLng.longitude;

                // Now you have the latitude and longitude of the clicked marker
                System.out.println("Marker clicked at: " + latitude + ", " + longitude);

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
}
