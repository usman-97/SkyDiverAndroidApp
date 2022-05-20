package com.usk.skydiver

import android.graphics.Canvas
import android.graphics.drawable.Drawable

class AnimatedObject(x: Int, y: Int, dx: Int, dy: Int, image: Drawable, width: Int, height: Int) : GameObject(
    x,
    y,
    dx,
    dy,
    image,
    width,
    height
) {

    override fun move(canvas: Canvas) {
        dy = this.startingDy + this.objectVelocityChange
        y -= dy

        setObjectContainer()
        if (y < (0 - width))
            reSpawn(x, startingY)

        drawGameObject(canvas)
    }

    // Reset default coordinate for the cloud object container
    override fun setObjectContainer() {
        this.objectContainer.left = 0
        this.objectContainer.top = y
        this.objectContainer.right = 0
        this.objectContainer.bottom = 0
    }
}