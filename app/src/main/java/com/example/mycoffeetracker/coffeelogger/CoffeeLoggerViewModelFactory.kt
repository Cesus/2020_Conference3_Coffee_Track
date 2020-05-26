package com.example.mycoffeetracker.coffeelogger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mycoffeetracker.database.CoffeeDatabaseDao

// boilerplate code that provides the key for the newest coffee log and CoffeeDatabaseDao to viewmodel
class CoffeeLoggerViewModelFactory(
    private val coffeeTrackingKey: Long,
    private val dataSource: CoffeeDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeLoggerViewModel::class.java)) {
            return CoffeeLoggerViewModel(coffeeTrackingKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}