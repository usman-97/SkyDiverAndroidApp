package com.usk.skydiver

class Timer(var limit: Int) {
    var seconds: Int = 0
        private set
    var hour: Int = 0
        private set

    fun startTimer()
    {
        if (seconds < limit)
            seconds++
        else
            seconds = 0

        if (seconds % 60 == 0)
            hour++
    }

    fun resetTimer()
    {
        seconds = 0
    }
}