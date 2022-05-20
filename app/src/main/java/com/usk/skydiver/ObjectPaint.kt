package com.usk.skydiver

import android.graphics.Color
import android.graphics.Paint

class ObjectPaint(var paint: Paint) {
    fun primayColour(): Paint
    {
        paint.color = Color.parseColor("#03A9F4")
        return paint
    }

    fun lightPrimaryColour(): Paint
    {
        paint.color = Color.parseColor("#B3E5FC")
        return paint
    }

    fun darkColour(): Paint
    {
        paint.color = Color.parseColor("#0f0f0f")
        return paint
    }

    fun redColour(): Paint
    {
        paint.color = Color.RED
        return paint
    }

    fun lightColour(): Paint
    {
        paint.color = Color.parseColor("#fffafa")
        return paint
    }

    fun darkGrayColour(): Paint
    {
        paint.color = Color.GRAY
        return paint
    }
}