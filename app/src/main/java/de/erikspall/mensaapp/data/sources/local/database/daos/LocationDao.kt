package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.room.*
import de.erikspall.mensaapp.data.sources.local.database.entities.Location
import de.erikspall.mensaapp.data.sources.local.database.entities.OpeningHours
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg locations: Location)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location): Long

    /*@Transaction
    @Query("SELECT * FROM locations")
    fun getFoodProvidersWithoutMenus(): Flow<List<FoodProviderWithoutMenus>>*/

    @Update
    suspend fun updateLocations(vararg locations: Location)

    @Query("SELECT EXISTS(SELECT * FROM locations WHERE lid = :lid COLLATE NOCASE)")
    suspend fun exists(lid: Long): Boolean

    @Delete
    suspend fun delete(location: Location)

    @Query("SELECT * FROM locations WHERE lid = :lid")
    suspend fun get(lid: Int): Location?

    @Query("SELECT * FROM locations")
    fun getAll(): Flow<List<Location>>
}