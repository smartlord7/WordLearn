package com.example.cmproject.util;
import com.example.cmproject.WordTranslation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FirebaseUploader {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final String tier;
    private final InputStream fileInputStream;

    public FirebaseUploader(String tier, InputStream fileInputStream) {
        this.tier = tier;
        this.fileInputStream = fileInputStream;
    }

    public void uploadToFirebase() {
        executor.execute(() -> {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(tier);

                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    // Split the line into Portuguese and English words
                    String[] words = line.split(";");
                    if (words.length == 2) {
                        String portugueseWord = words[0].trim();
                        String englishWord = words[1].trim();

                        // Create a WordTranslation object
                        WordTranslation wordTranslation = new WordTranslation();
                        wordTranslation.setLanguage1("portuguese");
                        wordTranslation.setWord1(portugueseWord);
                        wordTranslation.setLanguage2("english");
                        wordTranslation.setWord2(englishWord);

                        // Push the WordTranslation object to Firebase
                        databaseReference.push().setValue(wordTranslation);
                    }
                }

                // Close the reader
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception
            }
        });
    }
}

