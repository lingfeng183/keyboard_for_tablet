#!/bin/bash
# Build script for Keyboard For Tablet APK

set -e

echo "Building Keyboard For Tablet APK..."

# Check if ANDROID_HOME is set
if [ -z "$ANDROID_HOME" ]; then
    echo "Error: ANDROID_HOME is not set"
    exit 1
fi

# Create build directories
echo "Creating build directories..."
mkdir -p app/build/gen app/build/obj app/build/apk

# Generate R.java from resources
echo "Generating R.java..."
$ANDROID_HOME/build-tools/34.0.0/aapt package -f -m -J app/build/gen \
  -M app/src/main/AndroidManifest.xml -S app/src/main/res \
  -I $ANDROID_HOME/platforms/android-34/android.jar

# Compile Java sources
echo "Compiling Java sources..."
javac -source 1.8 -target 1.8 \
  -bootclasspath $ANDROID_HOME/platforms/android-34/android.jar \
  -d app/build/obj \
  app/build/gen/com/keyboard/tablet/R.java \
  app/src/main/java/com/keyboard/tablet/TabletKeyboardService.java

# Convert to DEX
echo "Converting to DEX..."
$ANDROID_HOME/build-tools/34.0.0/d8 \
  --lib $ANDROID_HOME/platforms/android-34/android.jar \
  --release --output app/build/apk \
  app/build/obj/com/keyboard/tablet/*.class

# Package APK
echo "Packaging APK..."
$ANDROID_HOME/build-tools/34.0.0/aapt package -f \
  -M app/src/main/AndroidManifest.xml \
  -S app/src/main/res \
  -I $ANDROID_HOME/platforms/android-34/android.jar \
  -F app/build/TabletKeyboard.unsigned.apk app/build/apk

# Add DEX to APK
echo "Adding DEX to APK..."
cd app/build/apk && jar -uf ../TabletKeyboard.unsigned.apk classes.dex && cd ../../..

# Align APK
echo "Aligning APK..."
$ANDROID_HOME/build-tools/34.0.0/zipalign -f -p 4 \
  app/build/TabletKeyboard.unsigned.apk \
  app/build/TabletKeyboard.aligned.apk

# Create debug keystore if it doesn't exist
if [ ! -f debug.keystore ]; then
    echo "Creating debug keystore..."
    keytool -genkeypair -keystore debug.keystore \
      -alias androiddebugkey -keypass android -storepass android \
      -keyalg RSA -keysize 2048 -validity 10000 \
      -dname "CN=Android Debug,O=Android,C=US"
fi

# Sign APK
echo "Signing APK..."
$ANDROID_HOME/build-tools/34.0.0/apksigner sign \
  --ks debug.keystore \
  --ks-key-alias androiddebugkey \
  --ks-pass pass:android \
  --key-pass pass:android \
  --out app/build/TabletKeyboard.apk \
  app/build/TabletKeyboard.aligned.apk

# Copy to root directory
cp app/build/TabletKeyboard.apk KeyboardForTablet-v1.0.apk

echo ""
echo "Build successful!"
echo "APK location: KeyboardForTablet-v1.0.apk"
echo ""

# Verify APK
echo "Verifying APK..."
$ANDROID_HOME/build-tools/34.0.0/apksigner verify -v KeyboardForTablet-v1.0.apk

echo ""
echo "APK verified successfully!"
