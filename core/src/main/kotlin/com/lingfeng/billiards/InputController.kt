package com.lingfeng.billiards

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter

/**
 * Input controller for billiards game.
 * Handles touch input for setting contact point, aiming, and shooting.
 *
 * State machine:
 * 1. IDLE: Waiting for touch on cue ball
 * 2. CONTACT_SET: Contact point set, waiting for drag to aim
 * 3. AIMING: Dragging to set direction and power
 * 4. SHOOTING: Released, ball is moving
 */
class InputController(
    private val cueBall: Ball,
    private val tableWidth: Float,
    private val tableHeight: Float,
    private val onShoot: (impulse: Vec2) -> Unit,
    private val onAimChange: (impulse: Vec2?) -> Unit
) : InputAdapter() {
    
    private enum class State {
        IDLE,           // Waiting for touch on cue ball
        CONTACT_SET,    // Contact point set, ready to drag
        AIMING          // Dragging to set power and direction
    }
    
    private var state = State.IDLE
    private var contactPoint: Vec2? = null
    private var dragStart: Vec2? = null
    private var currentImpulse: Vec2? = null
    
    private val maxPower = 30f
    
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val worldPos = screenToWorld(screenX, screenY)
        
        when (state) {
            State.IDLE -> {
                // Check if touch is on cue ball
                if (isTouchingBall(worldPos, cueBall)) {
                    contactPoint = worldPos.copy()
                    state = State.CONTACT_SET
                    onAimChange(null) // Clear any previous aim
                    return true
                }
            }
            State.CONTACT_SET -> {
                // Start dragging from current position
                dragStart = worldPos.copy()
                state = State.AIMING
                return true
            }
            State.AIMING -> {
                // Already aiming, ignore
            }
        }
        
        return false
    }
    
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (state != State.AIMING) return false
        
        val worldPos = screenToWorld(screenX, screenY)
        val start = dragStart ?: return false
        
        // Calculate impulse from drag vector
        val dx = worldPos.x - start.x
        val dy = worldPos.y - start.y
        
        // Limit power
        val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
        val power = Math.min(distance, maxPower)
        
        val impulse = if (distance > 0) {
            Vec2(
                (dx / distance) * power,
                (dy / distance) * power
            )
        } else {
            Vec2(0f, 0f)
        }
        
        currentImpulse = impulse
        onAimChange(impulse)
        
        return true
    }
    
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        when (state) {
            State.AIMING -> {
                // Shoot with current impulse
                currentImpulse?.let { impulse ->
                    if (impulse.length() > 0.5f) {
                        onShoot(impulse)
                    }
                }
                
                // Reset state
                reset()
                return true
            }
            State.CONTACT_SET -> {
                // Touch up without dragging, reset
                reset()
                return true
            }
            State.IDLE -> {
                // Nothing to do
            }
        }
        
        return false
    }
    
    /**
     * Get current contact point for rendering
     */
    fun getContactPoint(): Vec2? {
        return if (state != State.IDLE) contactPoint else null
    }
    
    /**
     * Reset input state
     */
    private fun reset() {
        state = State.IDLE
        contactPoint = null
        dragStart = null
        currentImpulse = null
        onAimChange(null)
    }
    
    /**
     * Force reset (e.g., when ball starts moving)
     */
    fun forceReset() {
        reset()
    }
    
    /**
     * Check if a world position is touching a ball
     */
    private fun isTouchingBall(pos: Vec2, ball: Ball): Boolean {
        val dx = pos.x - ball.position.x
        val dy = pos.y - ball.position.y
        val distanceSq = dx * dx + dy * dy
        val radiusSq = (ball.radius + 0.3f) * (ball.radius + 0.3f) // Add some tolerance
        return distanceSq <= radiusSq
    }
    
    /**
     * Convert screen coordinates to world coordinates
     */
    private fun screenToWorld(screenX: Int, screenY: Int): Vec2 {
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        
        val aspectRatio = screenWidth / screenHeight
        val tableAspectRatio = tableWidth / tableHeight
        
        val viewWidth: Float
        val viewHeight: Float
        
        if (aspectRatio > tableAspectRatio) {
            viewHeight = tableHeight + 4f
            viewWidth = viewHeight * aspectRatio
        } else {
            viewWidth = tableWidth + 4f
            viewHeight = viewWidth / aspectRatio
        }
        
        // Convert screen to normalized [0,1]
        val normX = screenX / screenWidth
        val normY = 1f - (screenY / screenHeight) // Flip Y
        
        // Convert to world coordinates
        val worldX = (normX * viewWidth) - (viewWidth - tableWidth) / 2f
        val worldY = (normY * viewHeight) - (viewHeight - tableHeight) / 2f
        
        return Vec2(worldX, worldY)
    }
}
