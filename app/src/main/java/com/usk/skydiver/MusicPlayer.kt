package com.usk.skydiver

import android.content.Context
import android.media.MediaPlayer

class MusicPlayer(var context: Context, var music: Int) {
    val maxVolume: Int = 100
    var volume: Float = 0f
    private var mediaPlayer: MediaPlayer = MediaPlayer.create(context, music)

    // Start playing music
    fun startMusic()
    {
        mediaPlayer.start()
    }

    // Pause or stop music
    fun pauseMusic()
    {
        mediaPlayer.pause()
    }

    // Loop the music
    fun loopMusic()
    {
        mediaPlayer.isLooping = true
    }

    // Change the volume
    fun setVolume(newVolume: Int)
    {
        volume = 1 - (Math.log((maxVolume - newVolume).toDouble()) / Math.log(maxVolume.toDouble())).toFloat()
        mediaPlayer.setVolume(volume, volume)
    }

}