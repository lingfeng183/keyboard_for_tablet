# Keyboard For Tablet

A custom Android keyboard application optimized for tablet devices.

## Features

- QWERTY layout optimized for tablets
- Supports basic text input
- Shift key for uppercase letters
- Delete and Done keys
- Space, comma, and period keys

## Installation

1. Download the `KeyboardForTablet-v1.0.apk` file
2. Install it on your Android device (requires Android 5.0 or higher)
3. Go to Settings → System → Languages & Input → Virtual Keyboard
4. Enable "Tablet Keyboard"
5. Select "Tablet Keyboard" as your input method

## Build Instructions

### Using Android SDK Tools (Manual Build)

The project can be built manually using Android SDK tools:

```bash
# Generate R.java from resources
$ANDROID_HOME/build-tools/34.0.0/aapt package -f -m -J app/build/gen \
  -M app/src/main/AndroidManifest.xml -S app/src/main/res \
  -I $ANDROID_HOME/platforms/android-34/android.jar

# Compile Java sources
javac -source 1.8 -target 1.8 \
  -bootclasspath $ANDROID_HOME/platforms/android-34/android.jar \
  -d app/build/obj \
  app/build/gen/com/keyboard/tablet/R.java \
  app/src/main/java/com/keyboard/tablet/TabletKeyboardService.java

# Convert to DEX
$ANDROID_HOME/build-tools/34.0.0/d8 \
  --lib $ANDROID_HOME/platforms/android-34/android.jar \
  --release --output app/build/apk \
  app/build/obj/com/keyboard/tablet/*.class

# Package APK
$ANDROID_HOME/build-tools/34.0.0/aapt package -f \
  -M app/src/main/AndroidManifest.xml \
  -S app/src/main/res \
  -I $ANDROID_HOME/platforms/android-34/android.jar \
  -F app/build/TabletKeyboard.unsigned.apk app/build/apk

# Add DEX to APK
cd app/build/apk && jar -uf ../TabletKeyboard.unsigned.apk classes.dex && cd ../../..

# Align APK
$ANDROID_HOME/build-tools/34.0.0/zipalign -f -p 4 \
  app/build/TabletKeyboard.unsigned.apk \
  app/build/TabletKeyboard.aligned.apk

# Sign APK
$ANDROID_HOME/build-tools/34.0.0/apksigner sign \
  --ks debug.keystore \
  --ks-key-alias androiddebugkey \
  --ks-pass pass:android \
  --key-pass pass:android \
  --out app/build/TabletKeyboard.apk \
  app/build/TabletKeyboard.aligned.apk
```

### Using Gradle (requires internet connection)

```bash
./gradlew assembleDebug
```

## Project Structure

```
app/
├── src/main/
│   ├── java/com/keyboard/tablet/
│   │   └── TabletKeyboardService.java  # Main keyboard service
│   ├── res/
│   │   ├── drawable/                   # Key backgrounds
│   │   ├── layout/                     # Keyboard layout
│   │   ├── mipmap-*/                   # App icons
│   │   ├── values/                     # Strings and colors
│   │   └── xml/                        # Keyboard configuration and QWERTY layout
│   └── AndroidManifest.xml
└── build.gradle
```

## Requirements

- Android 5.0 (API 21) or higher
- Android SDK 34 (for building)

## License

This project is provided as-is for educational and personal use.

