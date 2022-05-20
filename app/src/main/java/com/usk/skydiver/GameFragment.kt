package com.usk.skydiver

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.usk.skydiver.databinding.FragmentGameBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment(), SensorEventListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGameBinding
    private  lateinit var sensorManager: SensorManager
    private lateinit var accelerometer : Sensor
    private lateinit var navController: NavController
    private lateinit var resultViewModel: ResultViewModel
    private lateinit var musicPlayer: MusicPlayer

    var isGamePaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_game, container, false)

        // View binding with game fragment
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        // Play background music
        musicPlayer = MusicPlayer(requireActivity(), R.raw.game_music1)
        musicPlayer.setVolume(100)
        musicPlayer.startMusic()

        // Access view model
        resultViewModel = ViewModelProvider(requireActivity()).get(ResultViewModel::class.java)
        var result = resultViewModel.result.value

        val playIcon = context?.resources?.getDrawable(R.drawable.play_arrow_icon, null)
        val pauseIcon = context?.resources?.getDrawable(R.drawable.pause_icon, null)

        binding.startPauseButton.setOnClickListener {
            // If game is still on
            if (!binding.gameObjectView.isEnded)
            {
                // Show the button according to game state
                isGamePaused = if (!isGamePaused) {
                    binding.gameObjectView.stop()
                    binding.restartButton.visibility = View.VISIBLE
                    binding.quitButton.visibility = View.VISIBLE
                    binding.pauseOrEndContainer.visibility = View.VISIBLE
                    binding.startPauseButton.background = playIcon
                    true
                } else {
                    binding.gameObjectView.start()
                    binding.restartButton.visibility = View.INVISIBLE
                    binding.quitButton.visibility = View.INVISIBLE
                    binding.pauseOrEndContainer.visibility = View.INVISIBLE
                    binding.startPauseButton.background = pauseIcon
                    false
                }
            }
            else
            {
                binding.startPauseButton.visibility = View.INVISIBLE
                binding.restartButton.visibility = View.VISIBLE
                binding.quitButton.visibility = View.VISIBLE
                binding.pauseOrEndContainer.visibility = View.VISIBLE
            }
//            Log.d("test", "$isGamePaused ${binding.gameObjectView.isEnded}")
        }

        binding.restartButton.setOnClickListener {
            musicPlayer.pauseMusic()
            navController.navigate(R.id.action_gameFragment3_to_instructionFragment)
        }

        binding.quitButton.setOnClickListener {
            musicPlayer.pauseMusic()
            // When user quit or has completed the game
            // binding.gameObjectView.start()
            if (result != null)
            {
                // Set model values in result model view
                result.distanceCovered = binding.gameObjectView.distanceCovered
                result.totalDistance = binding.gameObjectView.totalDistance
                result.star = resultViewModel.calculateStars(result.distanceCovered, result.totalDistance)
                result.winningText = resultViewModel.generateResultText(result.star)

            }
            navController.navigate(R.id.action_gameFragment3_to_resultFragment)
        }

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // binding.gameObjectView.setSensorManager(sensorManager)

        // Accelerometer sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_STATUS_ACCURACY_LOW
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // When phone sensor is used
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null)
            return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER)
        {
            var x = event.values[0] // Get accelerometer sensor x coordinates
            var y = event.values[1] // Get accelerometer sensor y coordinates
            var z = event.values[2] // Get accelerometer sensor z coordinates

            // var myText = "%.2f".format(x) + " %.2f".format(y) + " %.2f".format(z)
            // Log.d("test", myText)

            var w: Int = 0
            var h: Int = 0
            binding.gameObjectView.doOnLayout {
                w = it.measuredWidth
                h = it.measuredHeight
            }

            binding.gameObjectView.skyDiverPosition = (w / 2f) + (x * 20f) // Calculate newDirection
            binding.gameObjectView.changeInNetForce = (h / 2f) + (z * 20f) // Calculate change in net force
            // Log.d("test", "${binding.gameObjectView.skyDiverPosition} ${binding.gameObjectView.changeInNetForce}")

            binding.gameObjectView.invalidate()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}