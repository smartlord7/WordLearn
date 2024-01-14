package com.example.cmproject.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public void isEmailExists(String email) {
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    boolean exists = !task.getResult().getSignInMethods().isEmpty();
                });
    }

    public void insertWord(String tier, String portuguese, String english) {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference tierRef = mDatabase.child("users").child(userId).child(tier.toLowerCase());

        String wordId = tierRef.push().getKey();
        Word word = new Word(portuguese, english);
        tierRef.child(wordId).setValue(word);
    }

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
        }
    }



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
