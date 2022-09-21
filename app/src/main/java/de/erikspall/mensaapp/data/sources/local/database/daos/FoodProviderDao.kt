package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProvider
import de.erikspall.mensaapp.data.sources.local.database.relationships.FoodProviderWithInfo
import de.erikspall.mensaapp.data.sources.local.database.relationships.FoodProviderWithoutMenus
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodProviderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg foodProviders: FoodProvider)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(foodProvider: FoodProvider): Long

    @Transaction
    @Query("SELECT * FROM food_providers")
    fun getFoodProvidersWithoutMenus(): Flow<List<FoodProviderWithoutMenus>>

    @Transaction
    @Query("SELECT * FROM food_providers WHERE food_provider_type_id = :tid AND location_id = :lid")
    fun getFoodProvidersByTypeAndLocation(tid: Long, lid: Long): Flow<List<FoodProviderWithoutMenus>>

    @Update
    suspend fun updateFoodProviders(vararg foodProviders: FoodProvider)

    @Query("SELECT EXISTS(SELECT * FROM food_providers WHERE fid = :fid COLLATE NOCASE)")
    suspend fun exists(fid: Long): Boolean

    @Delete
    suspend fun delete(foodProvider: FoodProvider)

    @Query("SELECT * FROM food_providers WHERE fid = :fid")
    suspend fun get(fid: Int): FoodProvider?

    @Query("SELECT * FROM food_providers")
    fun getAll(): Flow<List<FoodProvider>>

    @Query("SELECT * FROM food_providers WHERE fid = :fid")
    fun getFoodProvidersWithInfo(fid: Long): Flow<FoodProviderWithInfo>
}