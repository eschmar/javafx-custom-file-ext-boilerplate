module pew {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;

    opens com.example.pew to javafx.graphics, java.desktop;

    exports com.example.pew;
}
