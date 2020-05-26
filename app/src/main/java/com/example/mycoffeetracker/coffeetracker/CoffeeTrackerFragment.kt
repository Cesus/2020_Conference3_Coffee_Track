package com.example.mycoffeetracker.coffeetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.mycoffeetracker.R
import com.example.mycoffeetracker.database.CoffeeDatabase
import com.example.mycoffeetracker.databinding.FragmentCoffeeTrackerBinding

class CoffeeTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Get reference to binding object and inflate all fragment views
        val binding: FragmentCoffeeTrackerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_coffee_tracker, container, false)

        val application = requireNotNull(this.activity).application

        // Get instance of ViewModelFactory
        val dataSource = CoffeeDatabase.getInstance(application).coffeeDatabaseDao
        val viewModelFactory = CoffeeTrackerViewModelFactory(dataSource, application)

        // Get reference to ViewModel
        val coffeeTrackerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(CoffeeTrackerViewModel::class.java)

        binding.coffeeTrackerViewModel = coffeeTrackerViewModel

        binding.setLifecycleOwner(this)

        // Add an Observer on the state variable for Navigating when FAB is pressed.
        coffeeTrackerViewModel.navigateToCoffeeLogger.observe(this, Observer { coffee ->
            coffee?.let {
                this.findNavController().navigate(
                    CoffeeTrackerFragmentDirections
                        .actionCoffeeTrackerFragmentToCoffeeLoggerFragment(coffee.coffeeId))
                coffeeTrackerViewModel.doneNavigating()
            }
        })

        // TODO - add notification so that after 10mins (scientific number when caffeine takes affect), users can rate productivity

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)

        return binding.root
    }

}
