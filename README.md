# Namma-Platform

Namma-Platform is a Kannada-first, fully offline local railway station guide application designed specifically for rural passengers in Karnataka, India. Built natively for Android using Java, it provides essential train and platform information without requiring an internet connection.

## Features

* **Complete Offline Functionality**: All station and train data is bundled within the app. No internet permission is required, ensuring it works seamlessly in areas with poor or no network connectivity.
* **Station Selection**: Users can choose from major local stations including Mysuru, Mandya, Maddur, Ramanagara, and Bengaluru City.
* **Train Listings**: Displays the next available trains for the selected station, including train names, departure times, and designated platform numbers.
* **Visual Coach Layouts**: Provides a color-coded, horizontal scrollable layout showing exactly where each coach (Engine, General, Ladies, Sleeper, AC) will stop on the platform.
* **Kannada Voice Announcements**: Features built-in Text-To-Speech (TTS) that announces the train's arrival platform and the exact position of the General coach in Kannada, aiding users who may have difficulty reading.
* **High Contrast UI**: Designed with a high-contrast Blue and Yellow theme to ensure readability in bright sunlight and for visually impaired users.

## Technologies Used

* **Language**: Java
* **Platform**: Android SDK
* **Data Storage**: Local JSON parsing (built-in `JSONObject` and `JSONArray`)
* **Accessibility**: Android `TextToSpeech` API (Locale: Kannada, India)
* **Architecture**: Standard Android Activities, RecyclerView, and Custom Adapters without relying on third-party libraries (No Retrofit, Gson, or Glide).

## Screens

1. **Station Selector**: The launch screen featuring a dropdown to select the starting station.
2. **Train List**: A list displaying the upcoming trains, their departure times, and platforms for the chosen station.
3. **Coach Layout**: A detailed view of the train's coach arrangement and a button to trigger the Kannada audio announcement.

## Building the Project

1. Clone or download the repository.
2. Open the project in Android Studio.
3. Build the project using Gradle.
4. Run on an Android emulator or a physical device.

## License

This project is open-source and available for use and modification.
