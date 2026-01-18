package com.lingfeng.billiards

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.Gdx

/**
 * Renderer for billiards game using ShapeRenderer.
 * Draws the table, balls, and aiming line.
 *
 * @property tableWidth Width of the table in world units
 * @property tableHeight Height of the table in world units
 */
class Renderer(
    private val tableWidth: Float,
    private val tableHeight: Float
) {
    private val shapeRenderer = ShapeRenderer()
    
    // Colors
    private val tableColor = Color(0.1f, 0.5f, 0.1f, 1f) // Green felt
    private val cushionColor = Color(0.4f, 0.2f, 0.1f, 1f) // Brown cushions
    private val ballColor = Color.WHITE
    private val contactPointColor = Color.RED
    private val aimLineColor = Color(1f, 1f, 0f, 0.7f) // Semi-transparent yellow
    
    private val cushionWidth = 1f
    
    fun render(
        cueBall: Ball,
        contactPoint: Vec2?,
        trajectoryPoints: List<Vec2>?
    ) {
        shapeRenderer.projectionMatrix = setupCamera()
        
        // Draw table and cushions
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        // Draw table felt
        shapeRenderer.color = tableColor
        shapeRenderer.rect(0f, 0f, tableWidth, tableHeight)
        
        // Draw cushions (borders)
        shapeRenderer.color = cushionColor
        // Left cushion
        shapeRenderer.rect(-cushionWidth, -cushionWidth, cushionWidth, tableHeight + cushionWidth * 2)
        // Right cushion
        shapeRenderer.rect(tableWidth, -cushionWidth, cushionWidth, tableHeight + cushionWidth * 2)
        // Bottom cushion
        shapeRenderer.rect(0f, -cushionWidth, tableWidth, cushionWidth)
        // Top cushion
        shapeRenderer.rect(0f, tableHeight, tableWidth, cushionWidth)
        
        // Draw cue ball
        shapeRenderer.color = ballColor
        shapeRenderer.circle(cueBall.position.x, cueBall.position.y, cueBall.radius, 32)
        
        // Draw contact point if set
        contactPoint?.let {
            shapeRenderer.color = contactPointColor
            shapeRenderer.circle(it.x, it.y, 0.1f, 16)
        }
        
        shapeRenderer.end()
        
        // Draw trajectory line
        trajectoryPoints?.let { points ->
            if (points.size > 1) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                shapeRenderer.color = aimLineColor
                
                for (i in 0 until points.size - 1) {
                    val p1 = points[i]
                    val p2 = points[i + 1]
                    shapeRenderer.line(p1.x, p1.y, p2.x, p2.y)
                }
                
                shapeRenderer.end()
            }
        }
    }
    
    private fun setupCamera(): com.badlogic.gdx.math.Matrix4 {
        val camera = com.badlogic.gdx.graphics.OrthographicCamera()
        val aspectRatio = Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat()
        
        if (aspectRatio > tableWidth / tableHeight) {
            // Screen is wider than table
            camera.viewportHeight = tableHeight + 4f
            camera.viewportWidth = camera.viewportHeight * aspectRatio
        } else {
            // Screen is taller than table
            camera.viewportWidth = tableWidth + 4f
            camera.viewportHeight = camera.viewportWidth / aspectRatio
        }
        
        camera.position.set(tableWidth / 2f, tableHeight / 2f, 0f)
        camera.update()
        
        return camera.combined
    }
    
    fun dispose() {
        shapeRenderer.dispose()
    }
}
