<img src="https://github.com/eschmar/javafx-custom-file-ext-boilerplate/raw/master/src/main/resources/com/example/pew/icon.png" width="100" alt="Boilerplate recognising .pew files.">

# JavaFX Custom File Extension Viewer boilerplate

This is a boilerplate for creating a JavaFX application that is capable of handling a custom file extension. There are some nuances/caveats to getting this right (especially on MacOS), which are described in the [accompanying blog post](https://eschmann.io/posts/javafx-mac). In this example, the application is set up to handle `.pew` files with the bundle identifier `com.example.pew`. The [Makefile](https://github.com/eschmar/javafx-custom-file-ext-boilerplate/blob/master/Makefile) will generate a MacOS `.app` bundle by executing the following steps:

* Build the JavaFX application using jlink
* Generate a boilerplate `.app` bundle using `blueprint/launcher.applescript`
* Copy over the Java image from the build folder to the app bundle
* Replace the generated `Info.plist` file with a custom one from the `blueprint` folder
* Compile the sketch file to an icon set `AppIcon.iconset` and replace the default droplet icon with it

The result:

<img src="https://github.com/eschmar/javafx-custom-file-ext-boilerplate/raw/master/images/javafx-custom-file-extension.png" width="420" style="max-width: 420px;" alt="Boilerplate recognising .pew files.">

## requirements

* OpenJFX 12
* OpenJDK 12
* Gradle

## usage

```sh
# run:
make -B

# bundle mac app:
make -B plist icons bundle
```

## Caveats

* The java application itself will not carry the application name specified in the MacOS top menu bar, but will be named `java`. This should be solveable by providing the name as an option (`-Xdock:name="Example App"`) to the shell launcher, however that option has always been ignored for me.
* Since the applescript launcher needs to "stay open", in order to receive further file opening events after launch, the launcher will remain in the Mac dock.
* This is not a standard file structure, App Store would just reject this without further adjustments.
