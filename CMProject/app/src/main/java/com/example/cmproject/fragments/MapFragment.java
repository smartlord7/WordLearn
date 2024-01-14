package com.example.cmproject.fragments;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.example.cmproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeMap(view); // Call the method to initialize the map

    }

    private void initializeMap(View view) {
        FragmentContainerView mapContainer = view.findViewById(R.id.map);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        getChildFragmentManager().beginTransaction().replace(mapContainer.getId(), mapFragment).commit();

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

        int zoomInt = (int) googleMap.getCameraPosition().zoom;
        final int[] lastZoom = {-1};
        googleMap.setOnCameraMoveListener(() -> {
            int zoomInt1 = (int) googleMap.getCameraPosition().zoom;
            if (lastZoom[0] == -1) {
                lastZoom[0] = zoomInt1;
                return;
            }
            Log.d(TAG, "zoom= " + zoomInt1);

            // Access views using getView()
            View rootView = getView();
            if (zoomInt1 < 10) {
                rootView.findViewById(R.id.grid_small).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.grid_medium).setVisibility(View.GONE);
                rootView.findViewById(R.id.grid_large).setVisibility(View.GONE);
            } else if (zoomInt1 < 11) {
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
