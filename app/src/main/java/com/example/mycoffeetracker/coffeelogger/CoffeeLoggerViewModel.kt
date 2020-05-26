package com.example.mycoffeetracker.coffeelogger

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycoffeetracker.database.CoffeeDatabaseDao
import kotlinx.coroutines.*
import java.time.temporal.TemporalAmount

/**
 * ViewModel for CoffeeLoggerFragment.
 */

class CoffeeLoggerViewModel(
    private val coffeeTrackingKey: Long = 0L,
    val database: CoffeeDatabaseDao)  : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // clears the job
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // Encapsulation method to navigate to coffee tracker
    private val _navigateToCoffeeTracker = MutableLiveData<Boolean?>()
    val navigateToCoffeeTracker: LiveData<Boolean?>
        get() = _navigateToCoffeeTracker

    // function to stop navigating
    fun doneNavigating() {
        _navigateToCoffeeTracker.value = null
    }

    // when users choose an amount (imageview), adds the amounts to database and returns to coffeetracker
    fun onDone(amount: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val warmCoffee = database.get(coffeeTrackingKey) ?: return@withContext
                warmCoffee.coffeeAmount = amount
                database.update(warmCoffee)
            }

            _navigateToCoffeeTracker.value = true
        }

    }
}