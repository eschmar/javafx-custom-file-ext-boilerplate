CYAN=\033[0;36m
NC=\033[0m

APP_NAME=ExampleApp
BUILD_DIR=./build
TARGET=${BUILD_DIR}/${APP_NAME}.app
JLINK_IMAGE=${BUILD_DIR}/image
SKETCH_TOOL=/Applications/Sketch.app/Contents/Resources/sketchtool/bin/sketchtool

run:
	./gradlew build run

bundle:
	rm -rf ${BUILD_DIR}
	./gradlew jlink

	@echo "\n> ${CYAN}Bundling Mac .app${NC}\n"
	osacompile -o ${TARGET} -s blueprint/launcher.applescript
	rm -f ${TARGET}/Contents/Info.plist
	cp ./blueprint/Info.plist ${TARGET}/Contents
	cp -R ${JLINK_IMAGE}/* ${TARGET}/Contents
	mv ${TARGET}/Contents/bin/* ${TARGET}/Contents/MacOS
	rm -rf ${TARGET}/Contents/bin

	@echo "\n> ${CYAN}Add icon${NC}\n"
	rm -f ${TARGET}/Contents/Resources/droplet.icns
	cp ./blueprint/AppIcon.icns ${TARGET}/Contents/Resources

plist:
	plutil ./blueprint/Info.plist

icons:
	${SKETCH_TOOL} export artboards ./blueprint/AppIcon.sketch --output=./blueprint/AppIcon.iconset
	iconutil -c icns ./blueprint/AppIcon.iconset
