.PHONY: build install build-install clean lint run

# Default path to the Android SDK
ANDROID_HOME ?= /home/gk/Android/Sdk

# Build the debug APK
build:
	ANDROID_HOME=$(ANDROID_HOME) ./gradlew assembleDebug

# Install the built APK to a connected device
install:
	adb install -r app/build/outputs/apk/debug/app-debug.apk

# Build and then install
build-install: build install

# Clean the build outputs
clean:
	ANDROID_HOME=$(ANDROID_HOME) ./gradlew clean

# Run Android Lint
lint:
	ANDROID_HOME=$(ANDROID_HOME) ./gradlew lint

# Launch the app on the connected device
run:
	adb shell am start -n com.nammaplatform.app/.MainActivity
