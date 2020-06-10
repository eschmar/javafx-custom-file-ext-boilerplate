package com.example.pew;

import com.example.pew.helper.FileHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.Taskbar;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class App extends Application {
    /** Queue of files to open */
    private static final ConcurrentLinkedQueue<File> fileQueue = new ConcurrentLinkedQueue<>();

    /**
     * Listen for OPEN_FILE events while the application is running.
     */
    static {
        if (java.awt.Desktop.getDesktop().isSupported(Desktop.Action.APP_OPEN_FILE)) {
            Desktop.getDesktop().setOpenFileHandler(event -> {
                for (File file : event.getFiles()) enqueueFile(file.getAbsolutePath());
                Platform.runLater(App::launchFile);
            });
        }
    }

    /**
     * Parses path to a file and enqueues the file, if valid.
     *
     * @param path Path to local file.
     */
    public static void enqueueFile(String path) {
        final File file = FileHelper.parseInput(path);
        if (file == null) return;
        fileQueue.add(file);
    }

    @Override
    public void start(Stage stage) {
        initDockIcon();

        // Harvest cached FILE_OPEN events.
        while (Launcher.cachedFileOpenEvents != null && Launcher.cachedFileOpenEvents.size() > 0) {
            enqueueFile(Launcher.cachedFileOpenEvents.pop());
        }

        // Check for files in provided unnamed and named parameters
        final Parameters params = getParameters();
        for (String param : params.getUnnamed()) enqueueFile(param);
        for (Map.Entry<String, String> entry : params.getNamed().entrySet()) enqueueFile(entry.getValue());

        // If no file was provided, let the user select one
        if (fileQueue.size() == 0) {
            final File chosen = FileHelper.chooseFileFromStorage(null);
            if (chosen != null) enqueueFile(chosen.getAbsolutePath());
        }

        // If there is no file to be opened, terminate.
        if (fileQueue.size() == 0) {
            Platform.exit();
        }

        // Use primary stage for first file.
        if (fileQueue.size() > 0) spawnWindowForFile(fileQueue.poll(), stage);

        // Work down queue of files.
        launchFile();
    }

    /**
     * Launch a window for each file in the queue.
     */
    public static void launchFile() {
        while (!fileQueue.isEmpty()) spawnWindowForFile(fileQueue.poll(), null);
    }

    /**
     * Spawn new window for a file.
     */
    private static void spawnWindowForFile(File file, Stage stage) {
        if (stage == null) stage = new Stage();

        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        Scene scene = new Scene(new VBox(
                new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + "."),
                new Label("Opened file: " + file.getAbsolutePath())
        ), 320, 240);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Attempts to set the app icon in the task bar.
     */
    private void initDockIcon() {
        if (!Taskbar.isTaskbarSupported()) return;

        Image icon = null;

        try {
            icon = ImageIO.read(getClass().getResourceAsStream("icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (icon != null) Taskbar.getTaskbar().setIconImage(icon);
    }

    /**
     * {@inheritDoc}
     */
    public static void main(String[] args) {
        launch(args);
    }
}
