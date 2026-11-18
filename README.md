# FlashForge

A JavaFX study app: build decks of flashcards and multiple-choice questions, study them in flip mode, and take timed quizzes with saved score history.

**Team:** Jordan Davis, Tate Verzwyvelt, Ziyan Rajani

See `PROJECT_CONTEXT.md` for the plan, package layout, shared method names, and who owns what.

## Done so far
- `model/Deck`, `model/Flashcard`, `model/QuizResult`
- `data/DataManager` (static methods: `loadDecks()`, `saveDecks()`, `saveResult()`, `getHistory()`; returns a 5-card sample deck on first run)
