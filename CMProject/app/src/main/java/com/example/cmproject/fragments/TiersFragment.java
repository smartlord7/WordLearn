package com.example.cmproject.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cmproject.MainMenuActivity;
import com.example.cmproject.R;

public class TiersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tiers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnBronze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTierButtonClick(v, "Bronze");
            }
        });

        view.findViewById(R.id.btnSilver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTierButtonClick(v, "Silver");
            }
        });

        view.findViewById(R.id.btnGold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTierButtonClick(v, "Gold");
            }
        });

        view.findViewById(R.id.btnMaster).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTierButtonClick(v, "Master");
            }
        });
    }

    private void onTierButtonClick(View view, String tier) {
        ((MainMenuActivity) getActivity()).onTierButtonClick(view);
    }
}
