package com.flashforge.model;

import java.util.ArrayList;

// A named deck that holds a list of flashcards.
public class Deck {
    private String name;
    private ArrayList<Flashcard> cards;

    public Deck(String name) {
        this.name = name;
        cards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Returns a copy so other classes can't change the deck directly
    public ArrayList<Flashcard> getCards() {
        return new ArrayList<>(cards);
    }

    public void addCard(Flashcard card) {
        if (card == null) {
            return;
        }
        cards.add(card);
    }

    // Removes the card at the given position, ignoring bad indexes
    public void removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            cards.remove(index);
        }
    }

    public int size() {
        return cards.size();
    }

    // ListView uses this to show the deck's name
    @Override
    public String toString() {
        return name;
    }
}
