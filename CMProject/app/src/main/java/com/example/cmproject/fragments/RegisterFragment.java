package com.example.cmproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmproject.MainMenuActivity;
import com.example.cmproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    private EditText editTextUsername, editTextPassword, editTextEmail;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextEmail = view.findViewById(R.id.editTextEmail);

        mAuth = FirebaseAuth.getInstance();

        Button registerButton = view.findViewById(R.id.btnCreateAccount);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateAccountButtonClick(v);
            }
        });

        return view;
    }

    private void onCreateAccountButtonClick(View view) {
        String password = editTextPassword.getText().toString();
        String email = editTextEmail.getText().toString();

        // Check if password and email are not empty
        if (!password.isEmpty() && !email.isEmpty()) {
            // Save registration data to Firebase Authentication
            registerUser(email, password);
            ((MainMenuActivity) getActivity()).onLoginButtonClick(view);
        } else {
            // Show an error message if any field is empty
            showToast("All fields are required");
        }
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            showToast("Registration successful");
                        } else {
                            // If registration fails, display a message to the user.
                            showToast("Registration failed: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
