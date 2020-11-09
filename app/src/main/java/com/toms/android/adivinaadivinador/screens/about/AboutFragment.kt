package com.toms.android.adivinaadivinador.screens.about

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.databinding.FragmentAboutBinding
import com.toms.android.adivinaadivinador.databinding.ScoreFragmentBinding

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAboutBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_about,
                container,
                false
        )
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        return binding.root
    }

}