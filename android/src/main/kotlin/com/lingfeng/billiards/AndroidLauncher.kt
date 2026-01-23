package com.lingfeng.billiards

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

/**
 * Android launcher for the billiards game.
 */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val config = AndroidApplicationConfiguration().apply {
            useAccelerometer = false
            useCompass = false
            useWakelock = true
        }
        
        initialize(BilliardsGame(), config)
    }
}
