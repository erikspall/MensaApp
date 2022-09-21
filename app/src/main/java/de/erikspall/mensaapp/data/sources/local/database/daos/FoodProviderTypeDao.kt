package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.room.*
import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProviderType
import de.erikspall.mensaapp.data.sources.local.database.entities.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodProviderTypeDao {
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertAll(vararg types: FoodProviderType)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(type: FoodProviderType): Long

        /*@Transaction
        @Query("SELECT * FROM locations")
        fun getFoodProvidersWithoutMenus(): Flow<List<FoodProviderWithoutMenus>>*/

        @Update
        suspend fun updateFoodProviderTypes(vararg types: FoodProviderType)

        @Query("SELECT EXISTS(SELECT * FROM food_provider_types WHERE name = :name COLLATE NOCASE)")
        suspend fun exists(name: String): Boolean

        @Delete
        suspend fun delete(type: FoodProviderType)

        @Query("SELECT * FROM food_provider_types WHERE name = :name")
        suspend fun get(name: String): FoodProviderType?

        @Query("SELECT * FROM food_provider_types")
        fun getAll(): Flow<List<FoodProviderType>>

}