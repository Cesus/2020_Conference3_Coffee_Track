package com.example.mycoffeetracker.coffeelogger

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.mycoffeetracker.R
import com.example.mycoffeetracker.coffeetracker.CoffeeTrackerViewModel
import com.example.mycoffeetracker.database.CoffeeDatabase
import com.example.mycoffeetracker.databinding.FragmentCoffeeLoggerBinding
import kotlinx.android.synthetic.main.fragment_coffee_logger.*

class CoffeeLoggerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentCoffeeLoggerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_coffee_logger, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = CoffeeLoggerFragmentArgs.fromBundle(arguments!!)

        // Create an instance of the ViewModel Factory.
        val dataSource = CoffeeDatabase.getInstance(application).coffeeDatabaseDao
        val viewModelFactory = CoffeeLoggerViewModelFactory(arguments.coffeeTrackingKey, dataSource)

        val coffeeLoggerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(CoffeeLoggerViewModel::class.java)

        binding.coffeeLoggerViewModel = coffeeLoggerViewModel

        coffeeLoggerViewModel.navigateToCoffeeTracker.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    CoffeeLoggerFragmentDirections.actionCoffeeLoggerFragmentToCoffeeTrackerFragment())
                coffeeLoggerViewModel.doneNavigating()
            }
        })

         (activity as AppCompatActivity).supportActionBar?.title = "Please fill out the form"

        return binding.root
    }

}
