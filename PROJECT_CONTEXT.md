# FlashForge – Project Context

## Overview
FlashForge is a desktop study app built in JavaFX for a sophomore-level college programming course. Users create decks of flashcards and multiple-choice questions, review them in a flip-style study mode, and take timed quizzes that are scored. Decks and past quiz scores are saved to local files so progress carries over between runs.

**Team:** Jordan Davis, Tate Verzwyvelt, Ziyan Rajani

## Hard constraints
- Must run directly from the IDE (IntelliJ IDEA, the Run button). No installers, packaging, databases, servers, or user accounts.
- Code should look like a strong sophomore project: clear, well-commented, uses classes, ArrayLists, and event handling. Avoid advanced patterns (dependency injection, reflection, streams-heavy code, frameworks) unless they're clearly simpler.
- No third-party libraries beyond JavaFX unless the professor approves (e.g., Gson). Use plain text/CSV files for saving.

## Tech stack
- Java 21 (JDK 21)
- JavaFX 21 (OpenJFX) via Maven (`org.openjfx:javafx-controls`, `javafx-maven-plugin`)
- Build: Maven, created with IntelliJ's JavaFX project template
- UI written in pure Java code (no FXML), styled with one CSS file
- Version control: GitHub

## Package structure
```
src/main/java/com/flashforge/
├── App.java                 // entry point, holds the Stage, scene switching
├── model/
│   ├── Flashcard.java       // question, answer, options (ArrayList<String>), correctIndex
│   ├── Deck.java            // name, ArrayList<Flashcard> cards
│   └── QuizResult.java      // deckName, score, total, date
├── data/
│   └── DataManager.java     // loadDecks(), saveDecks(), saveResult(), getHistory()
└── ui/
    ├── MainMenuView.java    // Jordan
    ├── HistoryView.java     // Jordan
    ├── DeckManagerView.java // Tate
    ├── CardEditorView.java  // Tate
    ├── StudyView.java       // Ziyan
    ├── QuizView.java        // Ziyan
    └── ResultsView.java     // Ziyan
src/main/resources/com/flashforge/style.css
data/decks.csv, data/history.csv   // created at runtime in the project folder
```

## Shared contract (do not change without telling the group)
- `Flashcard(String question, String answer, ArrayList<String> options, int correctIndex)`; `options` may be empty for plain flashcards.
- `Deck(String name)` with `getCards()`, `addCard()`, `removeCard()`.
- `QuizResult(String deckName, int score, int total, LocalDate date)`.
- `DataManager` is the only class that touches files. Methods:
  - `ArrayList<Deck> loadDecks()`
  - `void saveDecks(ArrayList<Deck> decks)`
  - `void saveResult(QuizResult r)`
  - `ArrayList<QuizResult> getHistory()`
- Each view is a class that builds and returns a `Parent` via `getView()`. Navigation goes through `App`, e.g. `App.showMainMenu()`, `App.showStudy(Deck d)`, `App.showQuiz(Deck d)`.

## Ownership
- **Jordan Davis:** model classes, DataManager (save/load), App navigation, main menu, history screen, merging code
- **Tate Verzwyvelt:** deck manager (ListView; create, rename, delete), card editor form, input validation
- **Ziyan Rajani:** study mode (flip, next/previous), quiz mode (RadioButton + ToggleGroup, scoring, Timeline countdown, ProgressBar), results screen

## Screens
Main Menu → Deck Manager → Card Editor
Main Menu → pick deck → Study Mode or Quiz → Results
Main Menu → History

## Coding conventions
- Class names in PascalCase; methods and variables in camelCase
- Every class has a short header comment; non-obvious methods get one-line comments
- Validate user input and show `Alert` dialogs instead of crashing
- Handle missing or corrupt data files gracefully: start with an empty list and a sample deck
- Keep each file under about 250 lines where practical
