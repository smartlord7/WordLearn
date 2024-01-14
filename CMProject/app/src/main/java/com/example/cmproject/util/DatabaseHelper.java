package com.example.cmproject.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    // Firebase references
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public DatabaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // Example: Register a user
    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Handle successful registration
                    } else {
                        // Handle registration failure
                    }
                });
    }

    // Example: Check if a user with the given email exists
    public void isEmailExists(String email) {
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    boolean exists = !task.getResult().getSignInMethods().isEmpty();
                    // Handle the result
                });
    }

    // Example: Insert words into a specific tier table
    public void insertWord(String tier, String portuguese, String english) {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference tierRef = mDatabase.child("users").child(userId).child(tier.toLowerCase());

        String wordId = tierRef.push().getKey();
        Word word = new Word(portuguese, english);
        tierRef.child(wordId).setValue(word);
    }

    // Example: Upload words from a file to a specific tier
    public void uploadWordsFromFile(String tier, InputStream fileInputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                // Split the line into Portuguese and English words
                String[] words = line.split(";");
                if (words.length == 2) {
                    String portugueseWord = words[0].trim();
                    String englishWord = words[1].trim();

                    // Insert the words into the corresponding table
                    insertWord(tier, portugueseWord, englishWord);
                }
            }

            // Close the reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    // You may need additional methods based on your use case

    // Define the Word class to represent a word
    private static class Word {
        public String portuguese;
        public String english;

        public Word(String portuguese, String english) {
            this.portuguese = portuguese;
            this.english = english;
        }
    }
}
