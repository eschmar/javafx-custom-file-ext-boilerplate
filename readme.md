<img src="https://github.com/eschmar/javafx-custom-file-ext-boilerplate/raw/master/src/main/resources/com/example/pew/icon.png" width="100" alt="Boilerplate recognising .pew files.">

# JavaFX Custom File Extension Viewer boilerplate
This is a boilerplate for creating a JavaFX application that is capable of handling a custom file extension. There are some nuances/caveats to getting this right (especially on MacOS), which are described in the [accompanying blog post](https://eschmann.io/posts/javafx-mac). In this example, the application is set up to handle `.pew` files with the bundle identifier `com.example.pew`. The [Makefile](https://github.com/eschmar/javafx-custom-file-ext-boilerplate/blob/master/Makefile) will generate app bundles. The app simply prints out the file path:

<img src="https://github.com/eschmar/javafx-custom-file-ext-boilerplate/raw/master/images/javafx-custom-file-extension.png" width="420" style="max-width: 420px;" alt="Boilerplate recognising .pew files.">

## Requirements
* OpenJFX 14
* OpenJDK 14
* Gradle

## Features
* Associates a custom file type (.pew) with this application.
* Uses the `jpackage` tool from open jdk 14 to create application bundles.
* Capable of catching native apple `FILE_OPEN` events when double clicking files.
* Contains separete icons for file type and application.

## Caveats
* In order to catch all macOS FILE_OPEN events, the `Launcher` class needed to be introduced. While it allows to catch initial events, using it and launching JavaFX over the Main method means that AWT is the main GUI toolkit. The native system menu bar on mac os is no longer supported as a consequence.
* Mime-type is set to binary files. Change `*.properties` configurations to your needs.
* Windows platform not tested, yet. MacOS and Linux (Debian) are.

## Usage
```sh
# run:
make -B

# bundle mac app:
make -B jpackage_dawrin

# bundle linux app:
make -B jpackage_linux
```
