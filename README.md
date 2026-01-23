# Billiards LibGDX Prototype

A minimal but functional single-player billiards (cue ball) practice prototype built with libGDX and Kotlin for Android.

## Branch: feat/billiards-libgdx-prototype

This branch contains the initial billiards prototype implemented by @copilot.

## Features

### Implemented
- ✅ Table rendering with ShapeRenderer (green felt with brown cushions)
- ✅ Cue ball with physics simulation
- ✅ Touch input system:
  - Touch the cue ball to set contact point (打点)
  - Drag to set direction and power
  - Release to apply impulse and shoot
- ✅ Trajectory prediction with aiming line (yellow line showing predicted ball path)
- ✅ Simplified 2D physics:
  - Ball position and velocity updates
  - Friction decay (exponential)
  - Edge collisions with cushions (reflection with restitution)
- ✅ Modular code structure ready for future extensions

### Known Limitations
- Simplified physics model (no complex spin mechanics yet)
- No masse shots or advanced spin effects
- Only cue ball implemented (no object balls or ball-ball collisions)
- Basic trajectory prediction (doesn't account for advanced spin effects)
- Placeholder launcher icon

## Project Structure

```
.
├── core/                           # Core game logic (platform-independent)
│   └── src/main/kotlin/com/lingfeng/billiards/
│       ├── Ball.kt                 # Ball data class with Vec2 helper
│       ├── PhysicsEngine.kt        # 2D physics simulation
│       ├── TrajectoryPredictor.kt  # Trajectory prediction for aiming
│       ├── Renderer.kt             # ShapeRenderer-based rendering
│       ├── InputController.kt      # Touch input handling
│       └── BilliardsGame.kt        # Main game class
├── android/                        # Android-specific code
│   ├── src/main/
│   │   ├── kotlin/com/lingfeng/billiards/
│   │   │   └── AndroidLauncher.kt  # Android launcher
│   │   ├── AndroidManifest.xml
│   │   └── res/                    # Android resources
│   └── build.gradle.kts
├── .github/workflows/
│   └── android-build.yml           # CI/CD for Android builds
└── build.gradle.kts                # Root build configuration
```

## Building Locally

### Prerequisites
- JDK 11 or 17
- Android SDK with:
  - Platform tools
  - Build tools 33.x
  - Android API 33 (targetSdk)
  - Minimum API 21 (minSdk)

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Run on connected device/emulator
./gradlew android:run
```

The debug APK will be generated at:
```
android/build/outputs/apk/debug/android-debug.apk
```

## Downloading Pre-built APK

When the GitHub Actions workflow completes successfully:

1. Go to the repository's **Actions** tab
2. Click on the latest workflow run for this branch
3. Scroll down to **Artifacts**
4. Download **billiards-debug-apk**
5. Extract and install the APK on your Android device

**Note:** You may need to enable "Install from Unknown Sources" on your device.

## How to Play

1. **Launch the app** - You'll see a green billiards table with a white cue ball in the center
2. **Touch the cue ball** - This sets the contact point (打点) shown as a red dot
3. **Drag anywhere on screen** - This sets the shooting direction and power
   - The yellow trajectory line shows the predicted ball path
   - Longer drag = more power
4. **Release** - The ball shoots in the aimed direction
5. **Wait** - The ball will slow down due to friction and bounce off cushions
6. **Repeat** - Once the ball stops, you can shoot again

## Technical Details

### Physics
- **Friction:** Exponential velocity decay (coefficient: 0.98)
- **Restitution:** Cushion bounces at 80% energy retention
- **Time step:** Variable delta time from frame rate
- **Minimum velocity:** Balls stop when velocity < 0.05 units/sec

### Trajectory Prediction
- Simulates a copy of the physics state forward
- 50 steps with 0.1s time step (fast mode)
- Samples every 2nd position to reduce visual clutter
- Stops early if ball stops moving

### Coordinates
- Table: 20 units wide × 12 units tall
- Ball radius: 0.5 units
- Cushion width: 1 unit
- Power range: 0-30 units/sec

## Dependencies
- **libGDX:** 1.11.0 (game framework)
- **Kotlin:** 1.9.21
- **Gradle:** 8.5
- **Android Gradle Plugin:** 8.1.4
- **Target SDK:** 33
- **Min SDK:** 21

## Future Enhancements

Potential extensions (not yet implemented):
- [ ] Object balls and ball-ball collision physics
- [ ] Spin mechanics (top spin, back spin, side spin)
- [ ] Masse shots (curved trajectories)
- [ ] Pockets and scoring
- [ ] Multiple table layouts
- [ ] Training modes and challenges
- [ ] Sound effects
- [ ] Texture-based rendering for realistic graphics
- [ ] AI opponent

## License

This prototype was created for demonstration purposes.

---

**Implemented by:** @copilot  
**Date:** 2026-01-18

