package com.flashforge.model;

import java.util.ArrayList;

// One card: a question and its answer.
// If it has options, it is also a multiple-choice question,
// and correctIndex says which option is right (0 = first option).
public class Flashcard {
    private String question;
    private String answer;
    private ArrayList<String> options;
    private int correctIndex;

    // Multiple-choice card
    public Flashcard(String question, String answer, ArrayList<String> options, int correctIndex) {
        this.question = question;
        this.answer = answer;
        setOptions(options, correctIndex);
    }

    // Plain flip card with no options
    public Flashcard(String question, String answer) {
        this(question, answer, new ArrayList<>(), -1);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    // Returns a copy so other classes can't change the options directly
    public ArrayList<String> getOptions() {
        return new ArrayList<>(options);
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    // Sets the options and the correct one together so they always match
    public void setOptions(ArrayList<String> options, int correctIndex) {
        if (options == null) {
            this.options = new ArrayList<>();
        } else {
            this.options = new ArrayList<>(options);
        }
        if (correctIndex >= 0 && correctIndex < this.options.size()) {
            this.correctIndex = correctIndex;
        } else {
            this.correctIndex = -1;
        }
    }

    // True if this card can be used in quiz mode
    public boolean isMultipleChoice() {
        return options.size() >= 2 && correctIndex != -1;
    }

    // True if the chosen option number is the right one
    public boolean isCorrect(int choice) {
        return isMultipleChoice() && choice == correctIndex;
    }

    @Override
    public String toString() {
        return question;
    }
}
