package com.example.mycoffeetracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CoffeeDatabaseDao {
    @Insert
    fun insert(coffee: CoffeeTracking)

    @Update
    fun update(coffee: CoffeeTracking)

    @Query("SELECT * from coffee_tracker_quality_table WHERE coffeeId = :key")
    fun get(key: Long): CoffeeTracking?

    @Query("DELETE FROM coffee_tracker_quality_table")
    fun clear()

    @Query("SELECT * FROM coffee_tracker_quality_table ORDER BY coffeeId DESC")
    fun getAllCoffees(): LiveData<List<CoffeeTracking>>

    // gets the most recent coffee tracked
    @Query("SELECT * FROM coffee_tracker_quality_table ORDER BY coffeeId DESC LIMIT 1")
    fun getWarmCoffee(): CoffeeTracking?
}