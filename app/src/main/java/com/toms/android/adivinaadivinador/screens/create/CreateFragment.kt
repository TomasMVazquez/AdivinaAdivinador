package com.toms.android.adivinaadivinador.screens.create

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.database.ListDatabase
import com.toms.android.adivinaadivinador.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {

    private lateinit var binding: FragmentCreateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_create, container, false)
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_create,
                container,
                false
        )

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //get a reference to the application context.
        val application = requireNotNull(this.activity).application
        // get a reference to the DAO of the database
        val dataSource = ListDatabase.getInstance(application)
        //Create an instance of the viewModelFactory.
        val viewModelFactory = CreateViewModelFactory(dataSource.listDatabaseDao, application)
        // Get a reference to the ViewModel associated with this fragment.
        val createViewModel = ViewModelProvider(this,viewModelFactory).get(CreateViewModel::class.java)

        binding.createViewModel = createViewModel
        //Set the current activity as the lifecycle owner of the binding.
        binding.setLifecycleOwner(this)


        //Inicializamos el adapter y se lo pasamos al recyclerview
        /*val adapter = CreateListAdapter()
        binding.recyclerCreate.adapter = adapter*/

        //Le pasamos al adapter la data a mostrar

        binding.btnAddWord.setOnClickListener {
            val newWord = binding.editTextNewWord.text.toString()
            createViewModel._word.value = newWord
        }

        return binding.root
    }
}