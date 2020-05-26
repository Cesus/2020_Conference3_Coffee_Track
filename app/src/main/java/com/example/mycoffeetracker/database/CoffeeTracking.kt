package com.example.mycoffeetracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coffee_tracker_quality_table")
data class CoffeeTracking (

    @PrimaryKey(autoGenerate = true)
    var coffeeId: Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli: Long = startTimeMilli,

    // could have used coffeeQuantity but that looks too confusing and can be misread as coffeeQuality
    @ColumnInfo(name = "coffee_amount")
    var coffeeAmount: Int = -1,

    @ColumnInfo(name = "coffee_rating")
    var coffeeQuality: Int = -1

)