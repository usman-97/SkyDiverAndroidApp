package com.usk.skydiver

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.usk.skydiver.databinding.FragmentInstructionBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InstructionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InstructionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentInstructionBinding
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
        // return inflater.inflate(R.layout.fragment_instruction, container, false)

        // View binding with instruction fragment
        binding = FragmentInstructionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        // Start playing background music
        musicPlayer = MusicPlayer(requireActivity(), R.raw.game_music)
        musicPlayer.setVolume(100)
        musicPlayer.startMusic()

        // All the instructions about Newton's three laws of motion
        binding.mainInstructionHeading.text = "Consider Newton's three laws of motion to help J. Jones land safely."
        binding.mainInstructionHeading.setTextColor(Color.BLACK)

        binding.newtonFirstLaw.setTextColor(Color.WHITE)
        binding.newtonFirstLaw.text = "Newton’s first law states that if a body is at rest or moving at a constant speed in a straight line, it will remain at rest or keep moving in a straight line at constant speed unless it is acted upon by a force."

        binding.newtonSecondLaw.setTextColor(Color.WHITE)
        binding.newtonSecondLaw.text = "Force = Mass * Acceleration"

        binding.newtonThirdLaw.setTextColor(Color.WHITE)
        binding.newtonThirdLaw.text = "Newton’s third law states that when two bodies interact, they apply forces to one another that are equal in magnitude and opposite in direction."

        binding.startGameButton.setOnClickListener {
            musicPlayer.pauseMusic()
            navController.navigate(R.id.action_instructionFragment_to_gameFragment3)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InstructionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InstructionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}