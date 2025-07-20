package logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class FileOrganizer {
    private final File directory;
    private final MoveLogger logger;
    private final UndoManager undoManager;

    public FileOrganizer(File directory) {
        this.directory = directory;
        this.logger = MoveLogger.getInstance();
        this.undoManager = UndoManager.getInstance();
    }

    public void organize() throws IOException {
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory.");
        }

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile()) {
                String ext = getExtension(file.getName());
                File targetDir = new File(directory, ext.isEmpty() ? "others" : ext);
                if (!targetDir.exists()) targetDir.mkdir();

                File targetFile = resolveConflict(new File(targetDir, file.getName()));
                Files.move(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                logger.logMove(file, targetFile);
                undoManager.recordMove(file, targetFile);
            }
        }
    }

    private String getExtension(String name) {
        int i = name.lastIndexOf('.');
        return (i > 0) ? name.substring(i + 1).toLowerCase() : "";
    }

    private File resolveConflict(File target) {
        int counter = 1;
        String name = target.getName();
        String base = name.contains(".") ? name.substring(0, name.lastIndexOf('.')) : name;
        String ext = name.contains(".") ? name.substring(name.lastIndexOf('.')) : "";

        while (target.exists()) {
            target = new File(target.getParent(), base + "(" + counter + ")" + ext);
            counter++;
        }
        return target;
    }

    public void undo() throws IOException {
        undoManager.undoLast();
    }
}