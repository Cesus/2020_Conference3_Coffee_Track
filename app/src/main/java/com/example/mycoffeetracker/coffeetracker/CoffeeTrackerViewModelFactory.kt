package com.example.mycoffeetracker.coffeetracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mycoffeetracker.database.CoffeeDatabaseDao

// boilerplate code that provides CoffeeDatabaseDao and context to viewmodel
class CoffeeTrackerViewModelFactory(
    private val dataSource: CoffeeDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeTrackerViewModel::class.java)) {
            return CoffeeTrackerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
