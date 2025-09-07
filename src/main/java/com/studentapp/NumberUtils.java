package com.studentapp;

public class NumberUtils {
    // Game allows numbers 1..10
    public static boolean isValidGuess(int n) {
        return n >= 1 && n <= 10;
    }

    // helpful method we can unit test
    public static String compare(int guess, int target) {
        if (guess == target) return "equal";
        return guess < target ? "low" : "high";
    }
}
