package com.usk.skydiver

import android.graphics.Canvas
import android.graphics.drawable.Drawable

class Rocket(x: Int, y: Int, dx: Int, dy: Int, image: Drawable, width: Int, height: Int) : GameObject(x, y, dx, dy, image, width, height) {
//    var targetX: Int = 0
//    var targetY: Int = 0
    val coolDownTimerLimit: Int = 200
    var isLaunched: Boolean = false
    var isCoolDown: Boolean = true

    override fun move(canvas: Canvas) {
        if (isLaunched && isCoolDown)
        {
            if (y > (0 - height) && y < canvas.height)
            {
                y += dy
            }
            else
            {
                isLaunched = false
                isCoolDown = false
            }
//            if (targetX > x)
//                x += dx
//            else
//                x -= dx
//
//            if (targetY > y)
//                y += dy
//            else
//                y -= dy
//
//            if ((x > canvas.width || x < (0 - width)) || (y > canvas.height || y < (0 - height)))
//                isLaunched = false
        }

        drawGameObject(canvas)
    }

    fun startCoolDownTime(timer: Timer)
    {
        if (!isCoolDown)
        {
            timer.startTimer()
            if (timer.seconds == coolDownTimerLimit)
            {
                isCoolDown = true
                timer.resetTimer()
            }
        }
    }

    fun checkCollidedWithTarget(target: Enemy)
    {
        if (target is Helicopter)
        {
            if (this.objectContainer.intersect(target.objectContainer))
            {
                isLaunched = false
                isCoolDown = false
                target.isMoving = false
                target.reSpawn(0, 1500)
            }
        }
    }

    override fun setObjectContainer() {
        this.objectContainer.left = x + (width/4)
        this.objectContainer.top = y
        this.objectContainer.right = width + (x - 100)
        this.objectContainer.bottom = height + y
    }
}