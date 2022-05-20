package com.usk.skydiver

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import kotlin.random.Random

/**
 * GameObject class presents all game objects the game
 */
abstract class GameObject(
    var x: Int,
    var y: Int,
    var dx: Int,
    var dy: Int,
    var image: Drawable,
    var width: Int,
    var height: Int
)  {

    // Starting coordinates of game objects
    protected val startingX: Int = x
    protected val startingY: Int = 1500
    // protected var objectVelocityChange = 0
    protected val startingDx = dx
    protected val startingDy = dy
    var isGameEnded = false
    var objectVelocityChange: Int = 0
        private set
    // Container to detect collision with other objects
    var objectContainer: Rect = Rect()

    private var paint: Paint = Paint()
    var center: Int = 0

    abstract fun move(canvas: Canvas) // Abstract method implemented by all inherited classes

    // Set the container coordinates
    open fun setObjectContainer()
    {
        objectContainer.left = x
        objectContainer.top = y
        objectContainer.right = width + x
        objectContainer.bottom = height + y
    }

    // Draws the GameObject on the provided canvas
    open fun drawGameObject(canvas: Canvas)
    {
        setObjectContainer() // set object container
        paint.color = Color.parseColor("#B3E5FC") // colour of container
        // paint.color = Color.GREEN
        image.setBounds(x, y, (x + width) - 50, (y + height) - 10) // Draw image
        canvas.drawRect(objectContainer, paint) // Draw container
        image.draw(canvas)
    }

    // reset object to its starting position
    open fun reSpawn(x: Int, y: Int)
    {
        this.x = x
        this.y = y
    }

    open fun applyChangeInVelocity(velocityChange: Int)
    {
        if (velocityChange > 0)
            objectVelocityChange =  (velocityChange / 20)
    }

    fun spawnOnRandomXPosition(start: Int, end: Int)
    {
        x = Random.nextInt(start, end)
    }
}