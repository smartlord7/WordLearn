package com.example.cmproject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        editTextUsername = findViewById(R.id.editTextLoginUsername);
        editTextPassword = findViewById(R.id.editTextLoginPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    public void onLoginButtonClick(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        Log.d("LoginActivity", "Username: " + username + ", Password: " + password);

        // Check if the entered username and password are correct
        loginUser(username, password);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful, navigate to the activity with "Ranked Play" and "Scoreboard" buttons
                            Intent intent = new Intent(LoginActivity.this, LoggedInActivity.class);
                            startActivity(intent);
                            finish(); // Finish the LoginActivity to prevent going back
                        } else {
                            // If login fails, show an error message
                            showToast("Invalid username or password");
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

