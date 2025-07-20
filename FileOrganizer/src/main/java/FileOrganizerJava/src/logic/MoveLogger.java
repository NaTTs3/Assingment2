package logic;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoveLogger {
    private static final String LOG_FILE = "file_organizer.log";
    private static MoveLogger instance;

    private MoveLogger() {}

    public static MoveLogger getInstance() {
        if (instance == null) {
            instance = new MoveLogger();
        }
        return instance;
    }

    public void logMove(File from, File to) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write(time + " | Moved: " + from.getAbsolutePath() + " -> " + to.getAbsolutePath());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }
}