package com.flashforge.model;

import java.time.LocalDate;

// The score from one finished quiz.
public class QuizResult {
    private String deckName;
    private int score;
    private int total;
    private LocalDate date;

    public QuizResult(String deckName, int score, int total, LocalDate date) {
        this.deckName = deckName;
        this.score = score;
        this.total = total;
        this.date = date;
    }

    public String getDeckName() {
        return deckName;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public LocalDate getDate() {
        return date;
    }

    // Score as a whole-number percent, e.g. 7 out of 10 = 70
    public int getPercent() {
        if (total == 0) {
            return 0;
        }
        return Math.round(score * 100f / total);
    }

    @Override
    public String toString() {
        return date + "  " + deckName + "  " + score + "/" + total + " (" + getPercent() + "%)";
    }
}
