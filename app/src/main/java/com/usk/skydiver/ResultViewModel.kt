package com.usk.skydiver

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel() : ViewModel() {
    var result = MutableLiveData<Result>()

    init {
        result.value = Result(0, 0, 0.0f, "")
    }

    fun calculateStars(distanceCovered: Int, totalDistance: Int): Float
    {
        var star: Float = 0.0f
        var coveredDistance: Float = 0.0f

        if (distanceCovered >= totalDistance) {
            star = 3.0f
        }
        else if (distanceCovered >= (totalDistance / 2))
        {
            star = 2.0f
        }
        else
        {
            if (distanceCovered >= (totalDistance / 4))
                star = 1.0f
        }
        return star
    }

    fun generateResultText(star: Float): String
    {
        var winningText: String = ""

        if (star == 3.0f)
        {
            winningText = "Mission Passed"
        }
        else if (star == 2.0f)
        {
            winningText = "Better luck next time"
        }
        else
        {
            if (star == 1.0f)
                winningText = "Still long way to go..."
            else
                winningText = "Game Over"
        }
        return winningText
    }
}