package com.example.mycoffeetracker.coffeetracker

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.mycoffeetracker.database.CoffeeDatabaseDao
import com.example.mycoffeetracker.database.CoffeeTracking
import com.example.mycoffeetracker.formatCoffees
import kotlinx.coroutines.*

/**
 * ViewModel for CoffeeTrackerFragment.
 */

class CoffeeTrackerViewModel(
    val database: CoffeeDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    // Create variables to hold the most recent tracking and a list of all trackings
    private var warmCoffee = MutableLiveData<CoffeeTracking?>()
    private var allCoffees = database.getAllCoffees()

    val coffeesString = Transformations.map(allCoffees) { allCoffees ->
        formatCoffees(allCoffees, application.resources)
    }

    // Encapsulation live data technique to go from coffee tracker to coffee logger
    private val _navigateToCoffeeLogger = MutableLiveData<CoffeeTracking>()
    val navigateToCoffeeLogger: LiveData<CoffeeTracking>
        get() = _navigateToCoffeeLogger

    // resets the navigation event
    fun doneNavigating() {
        _navigateToCoffeeLogger.value = null
    }


    /*
    *   The following is to set up the database
    * */


    init {
        initializeWarmCoffee()
    }

    private fun initializeWarmCoffee() {
        uiScope.launch {
            warmCoffee.value = getWarmCoffeeFromDatabase()
        }
    }

    // If the start time and end time are the different, then we have an finished recording
    // isn't really useful for my program (copied from toy app)
    private suspend fun getWarmCoffeeFromDatabase(): CoffeeTracking? {
        return withContext(Dispatchers.IO) {
            var coffee = database.getWarmCoffee()
            if (coffee?.endTimeMilli != coffee?.startTimeMilli) {
                coffee = null
            }
            coffee
        }
    }

    // clears the database
    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    // updates the database
    private suspend fun update(coffee: CoffeeTracking) {
        withContext(Dispatchers.IO) {
            database.update(coffee)
        }
    }

    // insert a new instance of tracking
    private suspend fun insert(coffee: CoffeeTracking) {
        withContext(Dispatchers.IO) {
            database.insert(coffee)
        }
    }

    fun onStartTracking() {
        uiScope.launch {
            // Create a new coffee racking, which captures the current time,
            // and insert it into the database.
            val newCoffee = CoffeeTracking()

            insert(newCoffee)

            warmCoffee.value = getWarmCoffeeFromDatabase()

            // return@label syntax is used for specifying which function among
            // several nested ones this statement returns from.
            // In this case, we are specifying to return from launch(),
            // not the lambda.
            val oldCoffee = warmCoffee.value ?: return@launch

            // Update the coffee in the database to add the end time.
            oldCoffee.endTimeMilli = System.currentTimeMillis()

            update(oldCoffee)

            // Set state to navigate to the CoffeeLoggerFragment.
            _navigateToCoffeeLogger.value = oldCoffee
        }

    }

    // When clear button is clicked, function to clear database is called and sets the value of a new track to null
    fun onClear() {
        uiScope.launch {
            clear()
            warmCoffee.value = null
        }
    }

}