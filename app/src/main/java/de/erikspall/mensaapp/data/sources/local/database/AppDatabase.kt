package de.erikspall.mensaapp.data.sources.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.erikspall.mensaapp.data.sources.local.database.daos.FoodProviderDao
import de.erikspall.mensaapp.data.sources.local.database.daos.LocationDao
import de.erikspall.mensaapp.data.sources.local.database.daos.OpeningHoursDao
import de.erikspall.mensaapp.data.sources.local.database.daos.WeekdayDao
import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProvider
import de.erikspall.mensaapp.data.sources.local.database.entities.OpeningHours
import de.erikspall.mensaapp.data.sources.local.database.entities.Weekday
import de.erikspall.mensaapp.data.sources.local.database.entities.Location

@Database(entities = [FoodProvider::class, Location::class, OpeningHours::class, Weekday::class],
    version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun foodProviderDao(): FoodProviderDao
    abstract fun locationDao(): LocationDao
    abstract fun openingHoursDao(): OpeningHoursDao
    abstract fun weekdayDao(): WeekdayDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}