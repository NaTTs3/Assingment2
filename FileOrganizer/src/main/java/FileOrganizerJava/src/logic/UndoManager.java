package logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Stack;

public class UndoManager {
    private final Stack<Move> history = new Stack<>();
    private static UndoManager instance;

    private UndoManager() {}

    public static UndoManager getInstance() {
        if (instance == null) {
            instance = new UndoManager();
        }
        return instance;
    }

    public void recordMove(File from, File to) {
        history.push(new Move(from, to));
    }

    public void undoLast() throws IOException {
        if (history.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        Move move = history.pop();
        Files.move(move.to.toPath(), move.from.toPath());
        System.out.println("Undo: " + move.to + " -> " + move.from);
    }

    private static class Move {
        File from, to;
        Move(File from, File to) {
            this.from = from;
            this.to = to;
        }
    }
}