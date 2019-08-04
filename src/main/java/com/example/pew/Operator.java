package com.example.pew;

import javafx.stage.FileChooser;

import java.io.File;

public class Operator {
    public static final String[] EXTENSIONS = new String[] {"pew"};
    public static final String DEFAULT_EXTENSION = EXTENSIONS[0];
    public static final String[] MIME_TYPES = new String[] {"public.data, com.example.pew"};
    public static final String DEFAULT_FOLDER = "/Users/Shared";

    private static File openFile = null;

    private Operator() {}

    public static File getOpenFile() {
        return openFile;
    }

    /**
     * Checks whether an input parameter is a path to a valid file.
     *
     * @param input Console input parameter.
     * @return True if input was path to a valid file.
     */
    public static boolean parseInput(String input) {
        File file = new File(input);

        if (!file.exists() || file.isDirectory()) {
            return false;
        }

        boolean validExtension = false;
        for (String ext : EXTENSIONS) {
            if (!file.getName().endsWith(ext)) continue;
            validExtension = true;
            break;
        }

        if (!validExtension) return false;

        openFile = file;
        return true;
    }

    /**
     * Prompts the user to choose a file from storage.
     *
     * @return True if successfully found a file.
     */
    public static boolean chooseNewFile() {
        FileChooser fileChooser = new FileChooser();

        // Start from existing directory
        if (getOpenFile() != null) {
            fileChooser.setInitialDirectory(openFile.getParentFile());
        } else {
            File dir = new File(DEFAULT_FOLDER);
            if (dir.exists() && dir.isDirectory()) {
                fileChooser.setInitialDirectory(dir);
            }
        }

        // Set extension filter
        for (String extension : EXTENSIONS) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    extension + " files", "*." + extension
            ));
        }

        // Show open file dialog
        File candidateFile = fileChooser.showOpenDialog(null);
        if (candidateFile == null) return false;

        // Parse chosen file
        return parseInput(candidateFile.getAbsolutePath());
    }
}
