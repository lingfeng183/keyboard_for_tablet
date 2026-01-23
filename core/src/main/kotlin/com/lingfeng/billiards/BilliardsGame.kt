package com.lingfeng.billiards

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20

/**
 * Main game class for billiards prototype.
 * Implements ApplicationAdapter for libGDX lifecycle management.
 */
class BilliardsGame : ApplicationAdapter() {
    
    // Game components
    private lateinit var cueBall: Ball
    private lateinit var physicsEngine: PhysicsEngine
    private lateinit var trajectoryPredictor: TrajectoryPredictor
    private lateinit var renderer: Renderer
    private lateinit var inputController: InputController
    
    // Table dimensions
    private val tableWidth = 20f
    private val tableHeight = 12f
    
    // State
    private var currentImpulse: Vec2? = null
    private var trajectoryPoints: List<Vec2>? = null
    private var isSimulating = false
    
    override fun create() {
        // Initialize cue ball at center of table
        cueBall = Ball(
            position = Vec2(tableWidth / 2f, tableHeight / 2f),
            velocity = Vec2(0f, 0f),
            radius = 0.5f
        )
        
        // Initialize physics
        physicsEngine = PhysicsEngine(
            tableWidth = tableWidth,
            tableHeight = tableHeight,
            friction = 0.98f,
            restitution = 0.8f
        )
        
        // Initialize trajectory predictor
        trajectoryPredictor = TrajectoryPredictor(physicsEngine)
        
        // Initialize renderer
        renderer = Renderer(tableWidth, tableHeight)
        
        // Initialize input controller
        inputController = InputController(
            cueBall = cueBall,
            tableWidth = tableWidth,
            tableHeight = tableHeight,
            onShoot = { impulse ->
                shoot(impulse)
            },
            onAimChange = { impulse ->
                updateAim(impulse)
            }
        )
        
        Gdx.input.inputProcessor = inputController
    }
    
    override fun render() {
        // Clear screen
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        
        // Update physics if ball is moving
        if (isSimulating && cueBall.isMoving()) {
            val deltaTime = Gdx.graphics.deltaTime
            physicsEngine.update(cueBall, deltaTime)
        } else if (isSimulating) {
            // Ball stopped, allow new input
            isSimulating = false
        }
        
        // Render
        renderer.render(
            cueBall = cueBall,
            contactPoint = if (!isSimulating) inputController.getContactPoint() else null,
            trajectoryPoints = if (!isSimulating) trajectoryPoints else null
        )
    }
    
    override fun dispose() {
        renderer.dispose()
    }
    
    /**
     * Shoot the cue ball with the given impulse
     */
    private fun shoot(impulse: Vec2) {
        if (isSimulating) return
        
        cueBall.velocity.set(impulse)
        isSimulating = true
        trajectoryPoints = null
        currentImpulse = null
        inputController.forceReset()
    }
    
    /**
     * Update aim trajectory preview
     */
    private fun updateAim(impulse: Vec2?) {
        if (impulse == null) {
            trajectoryPoints = null
            currentImpulse = null
        } else {
            currentImpulse = impulse
            trajectoryPoints = trajectoryPredictor.predictFast(cueBall, impulse)
        }
    }
}
