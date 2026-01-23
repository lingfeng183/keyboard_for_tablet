package com.lingfeng.billiards

/**
 * Data class representing a billiard ball.
 *
 * @property position Current position (x, y) in world coordinates
 * @property velocity Current velocity vector (vx, vy) in units/second
 * @property omega Angular velocity in radians/second (for future spin mechanics)
 * @property radius Ball radius in world units
 * @property mass Ball mass (for future collision calculations)
 */
data class Ball(
    var position: Vec2,
    var velocity: Vec2,
    var omega: Float = 0f,
    val radius: Float = 0.5f,
    val mass: Float = 1f
) {
    /**
     * Check if ball is moving (has non-zero velocity)
     */
    fun isMoving(): Boolean {
        return velocity.length() > 0.001f
    }
    
    /**
     * Stop the ball completely
     */
    fun stop() {
        velocity.set(0f, 0f)
        omega = 0f
    }
}

/**
 * Simple 2D vector class for positions and velocities
 */
data class Vec2(
    var x: Float = 0f,
    var y: Float = 0f
) {
    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }
    
    fun set(other: Vec2) {
        this.x = other.x
        this.y = other.y
    }
    
    fun add(other: Vec2): Vec2 {
        return Vec2(x + other.x, y + other.y)
    }
    
    fun scale(factor: Float): Vec2 {
        return Vec2(x * factor, y * factor)
    }
    
    fun length(): Float {
        return kotlin.math.sqrt(x * x + y * y)
    }
    
    fun normalize(): Vec2 {
        val len = length()
        return if (len > 0) Vec2(x / len, y / len) else Vec2(0f, 0f)
    }
    
    fun dot(other: Vec2): Float {
        return x * other.x + y * other.y
    }
    
    fun copy(): Vec2 {
        return Vec2(x, y)
    }
}
