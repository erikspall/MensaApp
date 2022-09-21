package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.room.*
import de.erikspall.mensaapp.data.sources.local.database.entities.OpeningHours
import kotlinx.coroutines.flow.Flow

@Dao
interface OpeningHoursDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg openingHours: OpeningHours)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(openingHours: OpeningHours): Long

    /*@Transaction
    @Query("SELECT * FROM locations")
    fun getFoodProvidersWithoutMenus(): Flow<List<FoodProviderWithoutMenus>>*/

    @Update
    suspend fun updateOpeningHours(vararg openingHours: OpeningHours)

    @Query("SELECT EXISTS(SELECT * FROM opening_hours WHERE food_provider_id = :fid AND weekday_id = :wid COLLATE NOCASE)")
    suspend fun exists(fid: Long, wid: Long): Boolean

    @Delete
    suspend fun delete(openingHours: OpeningHours)

    @Query("SELECT * FROM opening_hours WHERE oid = :oid")
    suspend fun get(oid: Int): OpeningHours?


    @Query("SELECT * FROM opening_hours WHERE food_provider_id = :fid AND weekday_id = :wid")
    suspend fun get(fid: Long, wid: Long): OpeningHours?

    @Query("SELECT * FROM opening_hours")
    fun getAll(): Flow<List<OpeningHours>>
}