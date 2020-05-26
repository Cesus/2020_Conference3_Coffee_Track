package com.example.mycoffeetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CoffeeTracking::class], version = 1, exportSchema = false)
abstract class CoffeeDatabase : RoomDatabase() {

    abstract val coffeeDatabaseDao: CoffeeDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: CoffeeDatabase? = null

        fun getInstance(context: Context): CoffeeDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CoffeeDatabase::class.java,
                        "coffee_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}