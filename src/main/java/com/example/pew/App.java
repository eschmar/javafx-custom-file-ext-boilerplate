package com.example.pew;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class App extends Application {
    /**
     * Attempt listening for APP_OPEN_FILE events as soon as possible.
     * *ATTENTION:* This does not work with a shell launcher.
     */
    static {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.APP_OPEN_FILE)) {
            Desktop.getDesktop().setOpenFileHandler(event -> {
                for (File file : event.getFiles()) {
                    Operator.parseInput(file.getAbsolutePath());
                }
            });
        }
    }

    @Override
    public void start(Stage stage) {
        initTaskbar();

        // Check for files in provided console parameters
        Parameters params = getParameters();
        for (String param : params.getUnnamed()) Operator.parseInput(param);
        for (String param : params.getRaw()) Operator.parseInput(param);

        if (Operator.getOpenFile() == null && !Operator.chooseNewFile()) {
            // Failed...
            return;
        }

        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        // TODO: Show which file was opened.

        String chosenFile = "Opened file: " + Operator.getOpenFile().getAbsolutePath();

        Scene scene = new Scene(new VBox(l, new Label(chosenFile)), 320, 240);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Attempts to update the mac task bar with an icon.
     */
    private void initTaskbar() {
        if (!Taskbar.isTaskbarSupported()) return;

        Image icon = null;

        try {
            icon = ImageIO.read(getClass().getResourceAsStream("icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the icon for the dock
        if (icon != null) Taskbar.getTaskbar().setIconImage(icon);
    }

    public static void main(String[] args) {
        launch();
    }
}
