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

package com.toms.android.adivinaadivinador.screens.title

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.database.ListDatabase
import com.toms.android.adivinaadivinador.databinding.TitleFragmentBinding
import com.toms.android.adivinaadivinador.getSomeString

/**
 * Fragment for the starting or title screen of the app
 */
class TitleFragment : Fragment() {

    private lateinit var viewModel: TitleViewModel
    private lateinit var viewModelFactory: TitleViewModelFactory

    private lateinit var binding: TitleFragmentBinding

    override fun onResume() {
        super.onResume()
        viewModel.onShowMyList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater, R.layout.title_fragment, container, false)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val dataSource = ListDatabase.getInstance(requireActivity().application).listDatabaseDao

        viewModelFactory = TitleViewModelFactory(dataSource,requireActivity().application)

        viewModel = ViewModelProvider(this,viewModelFactory).get(TitleViewModel::class.java)

        binding.titleViewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        viewModel.showMyList.observe(viewLifecycleOwner, Observer { show ->
            if (show){
                binding.created.visibility = View.VISIBLE
                binding.created2.visibility = View.INVISIBLE
            }
        })

        viewModel.guessList.observe(viewLifecycleOwner, Observer { string ->
            when (string){
                this.getString(R.string.animals_list) -> {
                    viewModel.startFormatString()
                    binding.animals2.visibility = View.VISIBLE
                    binding.places2.visibility = View.INVISIBLE
                    binding.stuff2.visibility = View.INVISIBLE
                    if (viewModel.showMyList.value!!) binding.created2.visibility = View.INVISIBLE else binding.created2.visibility = View.GONE
                    binding.animalPic2.visibility = View.INVISIBLE
                    binding.random2.visibility = View.INVISIBLE
                    binding.createShadow.visibility = View.INVISIBLE
                }
                this.getString(R.string.places_list) -> {
                    viewModel.startFormatString()
                    binding.animals2.visibility = View.INVISIBLE
                    binding.places2.visibility = View.VISIBLE
                    binding.stuff2.visibility = View.INVISIBLE
                    if (viewModel.showMyList.value!!) binding.created2.visibility = View.INVISIBLE else binding.created2.visibility = View.GONE
                    binding.animalPic2.visibility = View.INVISIBLE
                    binding.random2.visibility = View.INVISIBLE
                    binding.createShadow.visibility = View.INVISIBLE
                }
                this.getString(R.string.stuff_list) -> {
                    viewModel.startFormatString()
                    binding.animals2.visibility = View.INVISIBLE
                    binding.places2.visibility = View.INVISIBLE
                    binding.stuff2.visibility = View.VISIBLE
                    if (viewModel.showMyList.value!!) binding.created2.visibility = View.INVISIBLE else binding.created2.visibility = View.GONE
                    binding.animalPic2.visibility = View.INVISIBLE
                    binding.random2.visibility = View.INVISIBLE
                    binding.createShadow.visibility = View.INVISIBLE
                }
                this.getString(R.string.created_list) -> {
                    viewModel.startFormatString()
                    binding.animals2.visibility = View.INVISIBLE
                    binding.places2.visibility = View.INVISIBLE
                    binding.stuff2.visibility = View.INVISIBLE
                    binding.created2.visibility = View.VISIBLE
                    binding.animalPic2.visibility = View.INVISIBLE
                    binding.random2.visibility = View.INVISIBLE
                    binding.createShadow.visibility = View.INVISIBLE
                }
                this.getString(R.string.anmals_picture_list) -> {
                    viewModel.startFormatString()
                    binding.animals2.visibility = View.INVISIBLE
                    binding.places2.visibility = View.INVISIBLE
                    binding.stuff2.visibility = View.INVISIBLE
                    if (viewModel.showMyList.value!!) binding.created2.visibility = View.INVISIBLE else binding.created2.visibility = View.GONE
                    binding.animalPic2.visibility = View.VISIBLE
                    binding.random2.visibility = View.INVISIBLE
                    binding.createShadow.visibility = View.INVISIBLE
                }
                this.getString(R.string.random_list) -> {
                    viewModel.startFormatString()
                    binding.animals2.visibility = View.INVISIBLE
                    binding.places2.visibility = View.INVISIBLE
                    binding.stuff2.visibility = View.INVISIBLE
                    if (viewModel.showMyList.value!!) binding.created2.visibility = View.INVISIBLE else binding.created2.visibility = View.GONE
                    binding.animalPic2.visibility = View.INVISIBLE
                    binding.random2.visibility = View.VISIBLE
                    binding.createShadow.visibility = View.INVISIBLE
                }
                this.getString(R.string.create_list) -> {
                    viewModel.endFormatString()
                    binding.animals2.visibility = View.INVISIBLE
                    binding.places2.visibility = View.INVISIBLE
                    binding.stuff2.visibility = View.INVISIBLE
                    if (viewModel.showMyList.value!!) binding.created2.visibility = View.INVISIBLE else binding.created2.visibility = View.GONE
                    binding.animalPic2.visibility = View.INVISIBLE
                    binding.random2.visibility = View.INVISIBLE
                    binding.createShadow.visibility = View.VISIBLE
                }
            }
        })

        /*binding.playGameButton.setOnClickListener {
            val action = TitleFragmentDirections.actionTitleToGame("HELLO WORLD")
            NavHostFragment.findNavController(this).navigate(action)
            //findNavController().navigate(TitleFragmentDirections.actionTitleToGame())
        }*/

        // Navigates play button is pressed
        viewModel.eventPlay.observe(viewLifecycleOwner, Observer { play ->
            if (play) {
                val action = TitleFragmentDirections.actionTitleToGame(viewModel.guessList.value.toString())
                NavHostFragment.findNavController(this).navigate(action)
                viewModel.onPlayComplete()
            }
        })

        viewModel.eventCreate.observe(viewLifecycleOwner, Observer { create ->
            if (create){
                val action = TitleFragmentDirections.actionTitleDestinationToCreateFragment()
                NavHostFragment.findNavController(this).navigate(action)
                viewModel.onCreateComplete()
            }
        })

        viewModel.guessList.observe(viewLifecycleOwner, Observer { newList ->
            if (newList.isNotEmpty()) {
                if (!newList.equals(getString(R.string.create_list))){
                    binding.playGameButton.visibility = View.VISIBLE
                    binding.createButton.visibility = View.INVISIBLE
                }else {
                    binding.playGameButton.visibility = View.INVISIBLE
                    binding.createButton.visibility = View.VISIBLE
                }
            }
        })

        return binding.root
    }
}
