package com.usk.skydiver

import android.graphics.Canvas
import android.graphics.drawable.Drawable

class PickableObject(x: Int, y: Int, dx: Int, dy: Int, image: Drawable, width: Int, height: Int) : GameObject(x, y, dx, dy, image, width, height) {
    var isVisible: Boolean = false

    override fun move(canvas: Canvas) {
        if (isVisible)
        {
            y -= dy
            if (y < (0 - height)) {
                reSpawn(this.startingX, this.startingY)
                isVisible = false
                // spawnOnRandomXPosition(50, (canvas.width - 50))
            }
            drawGameObject(canvas)
        }
    }

    override fun setObjectContainer() {
        this.objectContainer.left = x + (width/4)
        this.objectContainer.top = y
        this.objectContainer.right = width + (x - 100)
        this.objectContainer.bottom = height + y
    }
}