package com.example.cmproject;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextLoginUsername);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
    }

    public void onLoginButtonClick(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        Log.d("LoginActivity", "Username: " + username + ", Password: " + password);

        // Check if the entered username and password are correct
        if (isValidCredentials(username, password)) {
            // If correct, navigate to the activity with "Ranked Play" and "Scoreboard" buttons
            Intent intent = new Intent(this, LoggedInActivity.class);
            startActivity(intent);
            finish(); // Finish the LoginActivity to prevent going back
        } else {
            // If incorrect, show an error message
            showToast("Invalid username or password");
        }
    }

    private boolean isValidCredentials(String username, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Check if the username and password combination exists in the database
        boolean isValid = dbHelper.checkUserCredentials(username, password);

        Log.d("LoginActivity", "IsValid: " + isValid);

        // Close the database
        dbHelper.closeDatabase();

        return isValid;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
