package com.usk.skydiver

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.graphics.withClip

class Submarine(x: Int, y: Int, dx: Int, dy: Int, image: Drawable, width: Int, height: Int) : GameObject(
    x,
    y,
    dx,
    dy,
    image,
    width,
    height
) {

//    var isAnimationStarted = false
//    var isAnimationEnded = false

    override fun move(canvas: Canvas) {
        if (y > 400) {
            dy = this.startingDy + this.objectVelocityChange
            y -= dy
        }
        drawGameObject(canvas)
    }

    fun goUnderWater(canvas: Canvas)
    {
        dy = 10

        if (y < 1000)
            y += dy

        if (!isSubmarineOutOfBound(canvas) && y >= 1000)
            x += dx

        drawGameObject(canvas)
    }

    fun isSubmarineOutOfBound(canvas: Canvas): Boolean
    {
        if (x < (canvas.width + width))
            return false
        return true
    }

    override fun setObjectContainer() {
        this.objectContainer.left = 0
        this.objectContainer.top = y
        this.objectContainer.right = 0
        this.objectContainer.bottom = 0
    }
}