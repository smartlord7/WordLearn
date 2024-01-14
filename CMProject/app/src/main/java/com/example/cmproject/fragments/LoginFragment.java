package com.example.cmproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cmproject.MainMenuActivity;
import com.example.cmproject.fragments.HomeFragment;
import com.example.cmproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private EditText editTextUsername, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextUsername = view.findViewById(R.id.editTextLoginUsername);
        editTextPassword = view.findViewById(R.id.editTextLoginPassword);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClick(v);
            }
        });
    }

    private void onLoginButtonClick(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        Log.d("LoginFragment", "Username: " + username + ", Password: " + password);

        // Check if the entered username and password are correct
        loginUser(username, password,view);
    }

    private void loginUser(String email, String password, View view) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful, navigate to the activity with "Ranked Play" and "Scoreboard" buttons
                            ((MainMenuActivity) getActivity()).onLoginSuccessful(view);
                        } else {
                            // If login fails, show an error message
                            showToast("Invalid username or password");
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
