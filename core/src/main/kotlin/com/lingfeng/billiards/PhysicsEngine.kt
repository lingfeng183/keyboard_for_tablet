package com.lingfeng.billiards

/**
 * Simplified 2D physics engine for billiards simulation.
 * Handles ball movement, friction, and cushion collisions.
 *
 * @property tableWidth Width of the billiards table
 * @property tableHeight Height of the billiards table
 * @property friction Friction coefficient (0-1, higher = more friction)
 * @property restitution Coefficient of restitution for cushion collisions (0-1)
 */
class PhysicsEngine(
    private val tableWidth: Float,
    private val tableHeight: Float,
    private val friction: Float = 0.98f,
    private val restitution: Float = 0.8f
) {
    // Minimum velocity threshold - balls slower than this are stopped
    private val minVelocity = 0.05f
    
    /**
     * Update ball physics for one time step
     *
     * @param ball The ball to update
     * @param deltaTime Time step in seconds
     */
    fun update(ball: Ball, deltaTime: Float) {
        if (!ball.isMoving()) return
        
        // Apply friction (exponential decay)
        val frictionFactor = Math.pow(friction.toDouble(), deltaTime.toDouble() * 60.0).toFloat()
        ball.velocity.x *= frictionFactor
        ball.velocity.y *= frictionFactor
        
        // Stop if velocity is too low
        if (ball.velocity.length() < minVelocity) {
            ball.stop()
            return
        }
        
        // Update position
        ball.position.x += ball.velocity.x * deltaTime
        ball.position.y += ball.velocity.y * deltaTime
        
        // Handle cushion collisions
        handleCushionCollisions(ball)
    }
    
    /**
     * Check and handle collisions with table cushions (edges)
     */
    private fun handleCushionCollisions(ball: Ball) {
        val radius = ball.radius
        
        // Left cushion
        if (ball.position.x - radius < 0) {
            ball.position.x = radius
            ball.velocity.x = -ball.velocity.x * restitution
        }
        
        // Right cushion
        if (ball.position.x + radius > tableWidth) {
            ball.position.x = tableWidth - radius
            ball.velocity.x = -ball.velocity.x * restitution
        }
        
        // Bottom cushion
        if (ball.position.y - radius < 0) {
            ball.position.y = radius
            ball.velocity.y = -ball.velocity.y * restitution
        }
        
        // Top cushion
        if (ball.position.y + radius > tableHeight) {
            ball.position.y = tableHeight - radius
            ball.velocity.y = -ball.velocity.y * restitution
        }
    }
    
    /**
     * Create a copy of the physics state for trajectory prediction
     */
    fun clone(): PhysicsEngine {
        return PhysicsEngine(tableWidth, tableHeight, friction, restitution)
    }
}
