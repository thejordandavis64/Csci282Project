package com.flashforge.data;

import com.flashforge.model.Deck;
import com.flashforge.model.Flashcard;
import com.flashforge.model.QuizResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

// The only class that reads and writes files.
// Files live in a "data" folder inside the project folder (where IntelliJ runs the app).
//
// decks.txt format (fields separated by tabs):
//   DECK  <deck name>
//   CARD  <question>  <answer>  <correctIndex>  <option 1>  <option 2> ...
// history.txt format:
//   <deck name>  <score>  <total>  <date, e.g. 2026-11-18>
public class DataManager {
    private static final Path FOLDER = Paths.get("data");
    private static final Path DECKS_FILE = FOLDER.resolve("decks.txt");
    private static final Path HISTORY_FILE = FOLDER.resolve("history.txt");

    // Loads all decks. If there is no save file yet, returns a sample deck.
    public static ArrayList<Deck> loadDecks() {
        ArrayList<Deck> decks = new ArrayList<>();
        if (!Files.exists(DECKS_FILE)) {
            decks.add(createSampleDeck());
            return decks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DECKS_FILE.toFile()))) {
            Deck current = null;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t", -1);
                if (parts[0].equals("DECK") && parts.length >= 2) {
                    current = new Deck(unescape(parts[1]));
                    decks.add(current);
                } else if (parts[0].equals("CARD") && parts.length >= 4 && current != null) {
                    current.addCard(readCard(parts));
                }
                // Any other line is damaged, so it is skipped
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not read decks: " + e.getMessage());
        }

        if (decks.isEmpty()) {
            decks.add(createSampleDeck());
        }
        return decks;
    }

    // Overwrites the save file with the given decks.
    public static void saveDecks(ArrayList<Deck> decks) {
        try {
            Files.createDirectories(FOLDER);
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DECKS_FILE.toFile())))) {
                for (Deck deck : decks) {
                    out.println("DECK\t" + escape(deck.getName()));
                    for (Flashcard card : deck.getCards()) {
                        StringBuilder line = new StringBuilder("CARD");
                        line.append('\t').append(escape(card.getQuestion()));
                        line.append('\t').append(escape(card.getAnswer()));
                        line.append('\t').append(card.getCorrectIndex());
                        for (String option : card.getOptions()) {
                            line.append('\t').append(escape(option));
                        }
                        out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not save decks: " + e.getMessage());
        }
    }

    // Adds one quiz result to the end of the history file.
    public static void saveResult(QuizResult r) {
        try {
            Files.createDirectories(FOLDER);
            try (PrintWriter out = new PrintWriter(new FileWriter(HISTORY_FILE.toFile(), true))) {
                out.println(escape(r.getDeckName()) + "\t" + r.getScore() + "\t" + r.getTotal() + "\t" + r.getDate());
            }
        } catch (IOException e) {
            System.out.println("Could not save result: " + e.getMessage());
        }
    }

    // Returns every saved quiz result, oldest first.
    public static ArrayList<QuizResult> getHistory() {
        ArrayList<QuizResult> history = new ArrayList<>();
        if (!Files.exists(HISTORY_FILE)) {
            return history;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t", -1);
                if (parts.length < 4) {
                    continue;
                }
                try {
                    history.add(new QuizResult(unescape(parts[0]),
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            LocalDate.parse(parts[3])));
                } catch (RuntimeException e) {
                    // Damaged line, skip it
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read history: " + e.getMessage());
        }
        return history;
    }

    // A starter deck so the app has something to show on first run.
    public static Deck createSampleDeck() {
        Deck deck = new Deck("Java Basics (sample)");
        deck.addCard(new Flashcard("Which keyword creates a new object?", "new",
                new ArrayList<>(Arrays.asList("new", "make", "create", "build")), 0));
        deck.addCard(new Flashcard("Which method returns how many items an ArrayList has?", "size()",
                new ArrayList<>(Arrays.asList("length", "count()", "size()", "length()")), 2));
        deck.addCard(new Flashcard("What does a constructor's return type have to be?", "It has none",
                new ArrayList<>(Arrays.asList("void", "It has none", "int", "The class name")), 1));
        deck.addCard(new Flashcard("Which access modifier hides a field from other classes?", "private",
                new ArrayList<>(Arrays.asList("public", "static", "final", "private")), 3));
        deck.addCard(new Flashcard("What is the index of the first item in an ArrayList?", "0",
                new ArrayList<>(Arrays.asList("0", "1", "-1", "It depends")), 0));
        return deck;
    }

    // Turns one CARD line back into a Flashcard.
    private static Flashcard readCard(String[] parts) {
        String question = unescape(parts[1]);
        String answer = unescape(parts[2]);
        int correctIndex = Integer.parseInt(parts[3]);
        ArrayList<String> options = new ArrayList<>();
        for (int i = 4; i < parts.length; i++) {
            options.add(unescape(parts[i]));
        }
        return new Flashcard(question, answer, options, correctIndex);
    }

    // Tabs and line breaks would break the file format, so they are stored as \t and \n.
    private static String escape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n");
    }

    private static String unescape(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\\' && i + 1 < text.length()) {
                char next = text.charAt(++i);
                if (next == 't') {
                    result.append('\t');
                } else if (next == 'n') {
                    result.append('\n');
                } else {
                    result.append(next);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
