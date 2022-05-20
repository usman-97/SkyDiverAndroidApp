package com.usk.skydiver

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * SkyDiver class represents the playable Sky Diver character
 */
class SkyDiver(x: Int, y: Int, dx: Int, dy: Int, image: Drawable, width: Int, height: Int) : GameObject(
    x,
    y,
    dx,
    dy,
    image,
    width,
    height
) {
    var movementValue: Float = 0.0f
    var pickableObjects = mutableListOf<PickableObject>()

    private val noMovementValue: Float = 540f

    override fun move(canvas: Canvas) {
        moveRight(canvas) // Move to the right direction
        moveLeft() // move to the left direction
        // calculateCenter()
    }

    private fun moveRight(canvas: Canvas)
    {
        // Only move to right if it is not going out of
        // bound from right side
        if (x < (canvas.width - width) && movementValue < noMovementValue)
        {
            x += dx
        }

    }

    private fun moveLeft()
    {
        // Only move to left if it is not going out of
        // bound from left side
        if (x > 0 && movementValue > noMovementValue)
        {
            x -= dx
        }
    }

    // Sky diver container coordinates
    override fun setObjectContainer() {
        this.objectContainer.left = x + 100
        this.objectContainer.top = y + 60
        this.objectContainer.right = width + (x - 150)
        this.objectContainer.bottom = height + (y - 30)
    }

    // Check if sky diver is collided with enemy objects
    fun isCollidedWithEnemy(enemies: MutableList<Enemy>): Boolean
    {
        for (enemy: Enemy in enemies)
        {
            if (this.objectContainer.intersect(enemy.objectContainer))
            {
                return true
            }
        }
        return false
    }

    fun collectPickableObject(pickableObject: PickableObject)
    {
        if (objectContainer.intersect(pickableObject.objectContainer))
        {
            pickableObject.reSpawn(0, 1500)

            if (pickableObject.isVisible && pickableObjects.size < 11)
                pickableObjects.add(pickableObject)

            pickableObject.isVisible = false
        }
    }
}