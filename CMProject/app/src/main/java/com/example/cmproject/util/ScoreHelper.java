package com.example.cmproject.util;

public class ScoreHelper {
    // Function to dynamically calculate normalized time
    private static double calculateNormalizedTime(double totalTime) {
        // Normalize time to be between 0 and 1
        return totalTime > 0 ? 1.0 / (totalTime / 10000) : 1.0;
    }
    // Function to calculate performance value without predefined max time
    public static double calculatePerformance(double totalTime, int correctChoices, int totalChallenges) {
        // Calculate normalized values for time and correct choices
        double normalizedTime = calculateNormalizedTime(totalTime);
        double normalizedCorrectChoices = (double) correctChoices / totalChallenges;

        // Calculate the performance value using weighted averages
        double performance = calculateWeightedPerformance(normalizedTime, normalizedCorrectChoices);

        // Ensure the performance value is within the specified range
        return Math.max(0.0, Math.min(100.0, performance));
    }

    // Function to calculate weighted performance
    private static double calculateWeightedPerformance(double normalizedTime, double normalizedCorrectChoices) {
        // Adjust the weights based on your preference
        double weightCorrectChoices = 0.7;
        double weightTime = 0.3;

        // Calculate the performance value using weighted averages
        return (weightCorrectChoices * normalizedCorrectChoices + weightTime * (1 - normalizedTime)) * 100.0;
    }
}
