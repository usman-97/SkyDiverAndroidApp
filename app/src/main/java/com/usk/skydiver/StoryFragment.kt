package com.usk.skydiver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.usk.skydiver.databinding.FragmentStoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentStoryBinding
    private lateinit var navController: NavController
    private lateinit var musicPlayer: MusicPlayer

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
        // return inflater.inflate(R.layout.fragment_story, container, false)

        // View binding with story fragment
        binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        musicPlayer = MusicPlayer(requireActivity(), R.raw.game_music)
        musicPlayer.setVolume(100)
        musicPlayer.startMusic()

        binding.story.text = "Agent J. Jones was assigned to steal information from Mafia. He has managed to get the information but he was exposed to Mafia and now he needs to escape. If he managed to land safely then he can avoid Mafia."

        binding.nextButton.setOnClickListener {
            musicPlayer.pauseMusic()
            navController.navigate(R.id.action_storyFragment_to_instructionFragment)
        }

        binding.skipButton.setOnClickListener {
            musicPlayer.pauseMusic()
            navController.navigate(R.id.action_storyFragment_to_gameFragment3)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}