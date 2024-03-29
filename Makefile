CYAN=\033[0;36m
NC=\033[0m

APP_NAME=ExampleApp
VERSION=3.0.0

BUILD_DIR=./build
TARGET=${BUILD_DIR}/${APP_NAME}.app
JLINK_IMAGE=${BUILD_DIR}/image
SKETCH_TOOL=/Applications/Sketch.app/Contents/Resources/sketchtool/bin/sketchtool

default:
	./gradlew build run

jpackage_linux:
	./gradlew jlink

	# Temporarily copy icon file to root dir, since it needs to be in input dir.
	cp ./src/main/resources/com/example/pew/icon.png .

	${JAVA_HOME}/bin/jpackage \
		--app-version ${VERSION} \
		--copyright "Copyright 2022, example.com" \
		--description "Pew File Viewer" \
		--name "${APP_NAME}" \
		--dest build/distribution \
		--vendor "example.com" \
		--runtime-image build/image \
		--icon icon.png \
		--file-associations linux.properties \
		--module pew/com.example.pew.Launcher

	rm -f icon.png

jpackage_darwin:
	./gradlew jlink

	# Temporarily copy icon file to root dir, since it needs to be in input dir.
	cp ${BUILD_DIR}/res/FileIcon.icns ${BUILD_DIR}/..

	${JAVA_HOME}/bin/jpackage \
		--app-version ${VERSION} \
		--copyright "Copyright 2022, example.com" \
		--description "Pew File Viewer" \
		--name "${APP_NAME}" \
		--dest build/distribution \
		--vendor "example.com" \
		--runtime-image build/image \
		--icon build/res/AppIcon.icns \
		--mac-package-identifier com.example.pew \
		--mac-package-name "PewFileViewer" \
		--file-associations mac.properties \
		--module pew/com.example.pew.Launcher

	rm -f ${BUILD_DIR}/../FileIcon.icns

sketch_icons:
	${SKETCH_TOOL} export artboards ./raw/AppIcon.sketch --output=${BUILD_DIR}/res/AppIcon.iconset
	iconutil -c icns ${BUILD_DIR}/res/AppIcon.iconset
	rm -rf ${BUILD_DIR}/res/AppIcon.iconset

	${SKETCH_TOOL} export artboards ./raw/FileIcon.sketch --output=${BUILD_DIR}/res/FileIcon.iconset
	iconutil -c icns ${BUILD_DIR}/res/FileIcon.iconset
	rm -rf ${BUILD_DIR}/res/FileIcon.iconset

clean:
	rm -rf ${BUILD_DIR}
