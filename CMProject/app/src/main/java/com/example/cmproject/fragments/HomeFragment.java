package com.example.cmproject.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cmproject.MainMenuActivity;
import com.example.cmproject.R;
import com.example.cmproject.fragments.TiersFragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    /*private void onScoreboardButtonClick(View view) {
        // Handle Scoreboard button click
        Intent intent = new Intent(requireContext(), ScoreboardActivity.class);
        startActivity(intent);
    }*/
}


