package com.example.cmproject.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.cmproject.R;
import com.example.cmproject.WordTranslation;
import com.example.cmproject.util.ScoreHelper;
import com.example.cmproject.views.ChallengeMapViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GoldChallengeFragment extends Fragment {

    private TextView questionTextView;
    private Button[] answerButtons;
    private int totalWords = 6;  // Number of words to guess
    private int totalChoices = 6;  // Number of choices for each word
    private int currentWordIndex = 0;
    private int correctAnswers = 0;
    private TextView timerTextView;
    private long startTime;

    private Handler handler = new Handler();
    private boolean scoreDisplayed = false; // Variable to track if the score is displayed


    // List to store words retrieved from Firebase
    private List<WordTranslation> wordsList;

    private ChallengeMapViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold_challenge, container, false);
        timerTextView = view.findViewById(R.id.tvTimer);

        questionTextView = view.findViewById(R.id.questionTextView);
        answerButtons = new Button[totalChoices];
        for (int i = 0; i < totalChoices; i++) {
            int buttonId = getResources().getIdentifier("btnChoice" + (i + 1), "id", getActivity().getPackageName());
            answerButtons[i] = view.findViewById(buttonId);
        }

        loadWordsFromFirebase();
        showNextWord();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(ChallengeMapViewModel.class);
    }

    private void loadWordsFromFirebase() {
        wordsList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("gold");


        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    wordsList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String language1 = snapshot.child("language1").getValue(String.class);
                        String language2 = snapshot.child("language2").getValue(String.class);
                        String word1 = snapshot.child("word1").getValue(String.class);
                        String word2 = snapshot.child("word2").getValue(String.class);

                        WordTranslation word = new WordTranslation(0, language1, word1, language2, word2);
                        wordsList.add(word);
                    }

                    // Shuffle the wordsList to randomize the order
                    Collections.shuffle(wordsList);

                    // Once data is loaded and shuffled, show the first word
                    showNextWord();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        });
    }




    private void showNextWord() {
        if (currentWordIndex < totalWords && !wordsList.isEmpty()) {
            // Display the word in the chosen language
            questionTextView.setText(wordsList.get(currentWordIndex).getWord2());

            // Generate random choices (including the correct one)
            List<String> choices = generateRandomChoices(currentWordIndex);

            // Set choices to answer buttons
            for (int i = 0; i < totalChoices && i < choices.size(); i++) {
                answerButtons[i].setText(choices.get(i));
            }

            // Start the timer for the first word
            if (currentWordIndex == 0) {
                startTime = System.currentTimeMillis();
                updateTimer(); // Initial update
            }

            // Set click listeners for all answer buttons
            for (Button button : answerButtons) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkAnswer((Button) view);
                    }
                });
            }

            // Update the timer every second
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateTimer();
                    handler.postDelayed(this, 1000);
                }
            }, 1000);

        } else if (currentWordIndex == totalWords) {
            // All words guessed, calculate score and display results
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            double score = ScoreHelper.calculatePerformance(elapsedTime, correctAnswers, totalWords);
            scoreDisplayed = true; // Set the flag to true when showing the score
            displayResults(score);
        }
    }

    private void updateTimer() {
        if (!scoreDisplayed) { // Only update the timer if the score is not displayed
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            int seconds = (int) (elapsedTime / 1000);
            int hundredths = (int) ((elapsedTime / 10) % 100);
            timerTextView.setText(String.format(Locale.getDefault(), "Timer: %d.%02ds", seconds, hundredths));
        }
    }



    private List<String> generateRandomChoices(int wordIndex) {
        List<String> choices = new ArrayList<>();
        Set<String> uniqueChoices = new HashSet<>();

        // Add correct answer
        String correctAnswer = wordsList.get(wordIndex).getWord1();
        choices.add(correctAnswer);
        uniqueChoices.add(correctAnswer);

        // Retrieve random choices from the same language
        List<String> language1Words = new ArrayList<>();
        for (WordTranslation word : wordsList) {
            if (word.getLanguage1().equals(wordsList.get(wordIndex).getLanguage1())) {
                language1Words.add(word.getWord1());
            }
        }

        // Shuffle the list and add remaining choices
        Collections.shuffle(language1Words);

        for (String choice : language1Words) {
            if (uniqueChoices.size() < totalChoices) {
                // Add unique choices until totalChoices is reached
                if (!uniqueChoices.contains(choice)) {
                    choices.add(choice);
                    uniqueChoices.add(choice);
                }
            } else {
                break;
            }
        }

        // Shuffle the choices to randomize their order
        Collections.shuffle(choices);

        return choices;
    }


    private void checkAnswer(Button selectedButton) {
        // Increment the currentWordIndex for the next word
        currentWordIndex++;

        // Get the selected choice text
        String selectedChoice = selectedButton.getText().toString();

        // Get the correct choice text
        String correctChoice = wordsList.get(currentWordIndex - 1).getWord1();

        // Check if the selected choice is correct
        boolean isCorrect = selectedChoice.equals(correctChoice);

        // Update the UI based on correctness
        if (isCorrect) {
            // Correct answer, turn the button green
            selectedButton.setBackgroundColor(Color.GREEN);
            correctAnswers++;
        } else {
            // Wrong answer, turn the button red
            selectedButton.setBackgroundColor(Color.RED);
        }

        // Delay for a short time to show the color change
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Reset button colors
                resetButtonColors();
                // Show the next word
                showNextWord();
            }
        }, 1000);
    }

    private void resetButtonColors() {
        for (Button button : answerButtons) {
            button.setBackgroundColor(Color.parseColor("#757575")); // Reset to default color (white)
        }
    }

    @SuppressLint("DefaultLocale")
    private void displayResults(double score) {
        // Stop the timer
        handler.removeCallbacksAndMessages(null);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Display the results in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Quiz Results")
                .setMessage("Score: " + String.format("%.2f", score) + "\nCorrect Answers: " + correctAnswers + "\nTime Elapsed: " + String.format("%.2f", (double) elapsedTime / 1000) + " seconds")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedViewModel.setData(Double.toString(score));
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack();
                    }
                })
                .setCancelable(false) // Prevent dismissing by tapping outside the dialog
                .create()
                .show();
    }
}
