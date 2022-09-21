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

    @Query("SELECT EXISTS(SELECT * FROM locations WHERE name = :name COLLATE NOCASE)")
    suspend fun exists(name: String): Boolean

    @Delete
    suspend fun delete(location: Location)

    @Query("SELECT * FROM locations WHERE name = :name")
    suspend fun get(name: String): Location?

    @Query("SELECT * FROM locations")
    fun getAll(): Flow<List<Location>>
}