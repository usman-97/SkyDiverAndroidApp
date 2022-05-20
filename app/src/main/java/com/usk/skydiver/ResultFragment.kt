package com.usk.skydiver

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.usk.skydiver.databinding.FragmentResultBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentResultBinding
    private lateinit var resultViewModel: ResultViewModel
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
        // return inflater.inflate(R.layout.fragment_result, container, false)

        // View binding with result fragment
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        musicPlayer = MusicPlayer(requireActivity(), R.raw.game_music)
        musicPlayer.loopMusic()
        musicPlayer.setVolume(100)
        musicPlayer.startMusic()

        // Access result view model
        resultViewModel = ViewModelProvider(requireActivity()).get(ResultViewModel::class.java)
        // Access models values from resutl view model
        var result = resultViewModel.result.value

        if (result != null)
        {
            // Display the distance information
            binding.distanceText.text = "${result.distanceCovered}/${result.totalDistance}"
            binding.winningText.text = result.winningText

            // Display star according to player covered distance
            if (result.star >= 1.0f)
                binding.firstStar.setImageResource(R.drawable.filled_star)
            if (result.star >= 2.0f)
                binding.secondStar.setImageResource(R.drawable.filled_star)
            if (result.star >= 3.0f)
                binding.thirdStar.setImageResource(R.drawable.filled_star)

            // Log.d("test", "${result.star}")
        }

        binding.homeButton.setOnClickListener {
            navController.navigate(R.id.action_resultFragment_to_startFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}