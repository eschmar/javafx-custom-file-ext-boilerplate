package com.example.pew;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;

/**
 * Minimal launcher class to catch the initial FILE_OPEN events on macOS,
 * before the application is set up.
 *
 * **ATTENTION**: This class is currently a workaround. While it allows to catch
 * initial FILE_OPEN events, using it and launching JavaFX over the Main method
 * means that AWT is the main GUI toolkit. The system menu bar can
 * no longer be used on mac os as a consequence.
 *
 * https://bugs.openjdk.java.net/browse/JDK-8095227
 * https://bugs.openjdk.java.net/browse/JDK-8239590
 * https://bugs.openjdk.java.net/browse/JDK-8208652
 */
public class Launcher {
    /** Stores FILE_OPEN event paths temporarily until application has started. */
    public static final LinkedList<String> cachedFileOpenEvents = new LinkedList<>();

    /**
     * Attempt to listen for OPEN_FILE events as soon as possible.
     */
    static {
        if (java.awt.Desktop.getDesktop().isSupported(Desktop.Action.APP_OPEN_FILE)) {
            Desktop.getDesktop().setOpenFileHandler(event -> {
                for (File file : event.getFiles()) {
                    cachedFileOpenEvents.push(file.getAbsolutePath());
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    public static void main(String[] args) {
        // Launch application.
        App.main(args);

        // Terminate JVM without delay, once all windows are closed.
        Runtime.getRuntime().exit(0);
    }
}
