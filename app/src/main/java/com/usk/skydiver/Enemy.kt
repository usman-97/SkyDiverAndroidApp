package com.usk.skydiver

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import kotlin.random.Random

open class Enemy(x: Int, y: Int, dx: Int, dy: Int, image: Drawable, width: Int, height: Int) : GameObject(
    x,
    y,
    dx,
    dy,
    image,
    width,
    height
) {
    var isMoving: Boolean = false

    override fun move(canvas: Canvas) {
    }
}