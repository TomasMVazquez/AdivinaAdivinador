/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.toms.android.adivinaadivinador.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.database.ListDatabase
import com.toms.android.adivinaadivinador.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        val dataSource = ListDatabase.getInstance(requireActivity().application).listDatabaseDao

        // Get args using by navArgs property delegate
        val gameFragmentArgs by navArgs<GameFragmentArgs>()
        viewModelFactory = GameViewModelFactory(dataSource,requireActivity().application,gameFragmentArgs.list)

        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

        //Al realizar el binding directo desde el XML no es necesario el on click aqui
        /*
        binding.correctButton.setOnClickListener {
            viewModel.onCorrect()
        }
        binding.skipButton.setOnClickListener {
            viewModel.onSkip()
        }
        */

        viewModel.score.observe(viewLifecycleOwner, Observer {
            newScore -> binding.scoreText.text = newScore.toString()
        })

        //Podemos comentar este código debido a que estamos usamos data bindig para conectar directamente el xml con livedata
        /*
        viewModel.word.observe(viewLifecycleOwner, Observer {
            newWord -> binding.wordText.text = newWord })
        */

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if(hasFinished){
                gameFinished()
                viewModel.onGameFinishComplete()
            }

            // Buzzes when triggered with different buzz events
            viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
                if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                    buzz(buzzType.pattern)
                    viewModel.onBuzzComplete()
                }
            })

        })

        //Podemos comentar este código debido a que estamos usamos data bindig para conectar directamente el xml con livedata
        /*
        viewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime ->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime)

        })
         */

        return binding.root

    }


    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        findNavController(this).navigate(action)
    }

    /**
     * Given a pattern, this method makes sure the device buzzes
     */
    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }

    companion object {
        private const val TAG = "TMV:"
    }
}
