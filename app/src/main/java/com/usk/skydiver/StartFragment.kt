package com.usk.skydiver

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.usk.skydiver.databinding.FragmentStartBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var binding : FragmentStartBinding
@SuppressLint("StaticFieldLeak")
private  lateinit var navController: NavController
@SuppressLint("StaticFieldLeak")
private lateinit var musicPlayer: MusicPlayer

/**
 * A simple [Fragment] subclass.
 * Use the [StartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // private val playIcon: Icon = Icon()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_start, container, false)

        // View binding with start fragment
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        // Play backgroud music using MusicPlayer class
        musicPlayer = MusicPlayer(requireContext(), R.raw.game_music)
        musicPlayer.loopMusic()
        musicPlayer.setVolume(100)
        musicPlayer.startMusic()

        // When start button is clicked then stop the background music and
        // navigate to story fragment
        binding.startButton.setOnClickListener {
            musicPlayer.pauseMusic()
            navController.navigate(R.id.action_startFragment_to_storyFragment)
        }

        binding.helpButton.setOnClickListener {
            musicPlayer.pauseMusic()
            navController.navigate(R.id.action_startFragment_to_helpFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}