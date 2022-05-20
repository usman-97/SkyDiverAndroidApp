package com.usk.skydiver

import android.graphics.Canvas
import android.graphics.drawable.Drawable

class Icon(var width: Int, var height: Int, var icon: Drawable) {
    // Draw icon
    fun drawIcon(x: Int, y:Int, canvas: Canvas)
    {
        icon.setBounds(x, y, width + x, height + y)
        icon.draw(canvas)
    }
}