package com.example.pew.helper;

import com.example.pew.Config;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Locale;

public class FileHelper {
    /**
     * Prompts the user to choose a file from storage.
     *
     * @param base Base path to choose from.
     * @return Path to file.
     */
    public static File chooseFileFromStorage(final File base) {
        FileChooser fileChooser = new FileChooser();

        // Start from existing directory
        if (base != null) {
            if (base.isDirectory()) fileChooser.setInitialDirectory(base);
            else fileChooser.setInitialDirectory(base.getParentFile());
        } else {
            File dir = new File(System.getProperty("user.home"));

            if (dir.exists() && dir.isDirectory()) {
                fileChooser.setInitialDirectory(dir);
            }
        }

        // Set extension filter
        for (String extension : Config.EXTENSIONS) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                String.format(Locale.ROOT, ".%s file type", extension), "*." + extension
            ));
        }

        // Show open file dialog
        return fileChooser.showOpenDialog(null);
    }

    /**
     * Checks whether an input parameter is a path to a valid file.
     *
     * @param input Console input parameter.
     * @return True if input was a valid path.
     */
    public static File parseInput(String input) {
        File file = new File(input);

        if (!file.exists() || file.isDirectory() || !file.getName().endsWith(Config.DEFAULT_EXTENSION)) {
            return null;
        }

        boolean validExtension = false;
        for (String ext : Config.EXTENSIONS) {
            if (!file.getName().endsWith(ext)) continue;
            validExtension = true;
            break;
        }

        if (!validExtension) {
            return null;
        }

        return file;
    }
}
