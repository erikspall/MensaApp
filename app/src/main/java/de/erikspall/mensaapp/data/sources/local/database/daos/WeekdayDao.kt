package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.room.*
import de.erikspall.mensaapp.data.sources.local.database.entities.OpeningHours
import de.erikspall.mensaapp.data.sources.local.database.entities.Weekday
import kotlinx.coroutines.flow.Flow

@Dao
interface WeekdayDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg weekday: Weekday)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weekday: Weekday): Long

    /*@Transaction
    @Query("SELECT * FROM locations")
    fun getFoodProvidersWithoutMenus(): Flow<List<FoodProviderWithoutMenus>>*/

    @Update
    suspend fun updateWeekday(vararg weekday: Weekday)

    @Query("SELECT EXISTS(SELECT * FROM weekday WHERE name = :name COLLATE NOCASE)")
    suspend fun exists(name: String): Boolean

    @Delete
    suspend fun delete(weekday: Weekday)

    @Query("SELECT * FROM weekday WHERE name = :name")
    suspend fun get(name: String): Weekday?

    @Query("SELECT * FROM weekday")
    fun getAll(): Flow<List<Weekday>>
}