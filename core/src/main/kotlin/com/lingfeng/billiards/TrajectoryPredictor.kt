package com.lingfeng.billiards

/**
 * Trajectory predictor that simulates physics forward to generate aiming line points.
 *
 * @property physicsEngine Physics engine to use for simulation
 */
class TrajectoryPredictor(
    private val physicsEngine: PhysicsEngine
) {
    /**
     * Predict trajectory for a given impulse applied to a ball.
     *
     * @param ball The ball to predict trajectory for (not modified)
     * @param impulse The impulse vector to apply
     * @param maxSteps Maximum number of simulation steps
     * @param stepSize Time step size for each simulation step
     * @return List of predicted positions along the trajectory
     */
    fun predict(
        ball: Ball,
        impulse: Vec2,
        maxSteps: Int = 100,
        stepSize: Float = 0.05f
    ): List<Vec2> {
        val positions = mutableListOf<Vec2>()
        
        // Create a copy of the ball for simulation
        val simBall = Ball(
            position = ball.position.copy(),
            velocity = impulse.copy(),
            omega = ball.omega,
            radius = ball.radius,
            mass = ball.mass
        )
        
        // Simulate forward and collect positions
        positions.add(simBall.position.copy())
        
        for (i in 0 until maxSteps) {
            if (!simBall.isMoving()) break
            
            physicsEngine.update(simBall, stepSize)
            
            // Sample positions at regular intervals to avoid too many points
            if (i % 2 == 0) {
                positions.add(simBall.position.copy())
            }
        }
        
        return positions
    }
    
    /**
     * Predict trajectory with fewer, larger steps for performance
     */
    fun predictFast(ball: Ball, impulse: Vec2): List<Vec2> {
        return predict(ball, impulse, maxSteps = 50, stepSize = 0.1f)
    }
}
