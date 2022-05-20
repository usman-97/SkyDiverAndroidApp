package com.usk.skydiver

import android.graphics.Canvas
import android.graphics.drawable.Drawable

class Helicopter(x: Int, y: Int, dx: Int, dy: Int, image: Drawable, width: Int, height: Int) : Enemy(x, y, dx, dy, image, width, height) {

    override fun move(canvas: Canvas) {
        if (isMoving)
        {
            if (dy < 0)
                dy = this.startingDy - this.objectVelocityChange
            else
                dy = this.startingDy + this.objectVelocityChange

            // x += dx
            y -= dy
            // calculateCenter()

            if (y < (350 - width))
            {
                reSpawn(startingX, startingY)
                isMoving = false
                // resetCollisionValues()
            }

            drawGameObject(canvas)
        }
    }

    override fun setObjectContainer() {
        super.setObjectContainer()
        this.objectContainer.top = y + 60
        this.objectContainer.right = width + (x - 100)
        this.objectContainer.bottom = height + (y - 30)
    }
}