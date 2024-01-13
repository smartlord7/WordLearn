package com.example.cmproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextEmail;
    private DatabaseHelper dbHelper; // Declare dbHelper as a class variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);

        dbHelper = new DatabaseHelper(this); // Initialize dbHelper in onCreate
    }

    public void onRegisterButtonClick(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String email = editTextEmail.getText().toString();

        // Check if username, password, and email are not empty
        if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
            // Save registration data to the database (you need to implement a database helper for this)
            saveToDatabase(username, password, email);

            // Perform any additional actions after successful registration

            // Finish the activity (optional)
            finish();
        } else {
            // Show an error message if any field is empty
            // You can customize this part based on your requirements
            // For example, display a Toast or set an error on the corresponding EditText
            // indicating that the field is required.
        }
    }

    private void saveToDatabase(String username, String password, String email) {
        // Check if username or email already exists
        if (dbHelper.isUsernameExists(username)) {
            // Show a message indicating that the username is already in use
            showToast("Username already in use");
        } else if (dbHelper.isEmailExists(email)) {
            // Show a message indicating that the email is already in use
            showToast("Email already in use");
        } else {
            // Insert the user data into the database
            dbHelper.insertUser(username, password, email);

            Log.d("DatabaseHelper", "User registered.");
            // Perform any additional actions after successful registration
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        // Close the database when the activity is destroyed
        dbHelper.closeDatabase();
        super.onDestroy();
    }
}
