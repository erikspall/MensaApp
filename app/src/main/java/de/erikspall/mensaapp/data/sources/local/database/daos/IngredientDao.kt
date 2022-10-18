package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.room.*
import de.erikspall.mensaapp.data.sources.local.database.entities.IngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg ingredientEntities: IngredientEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ingredientEntity: IngredientEntity)

    @Update
    suspend fun updateIngredients(vararg ingredientEntities: IngredientEntity)

    @Query("SELECT EXISTS(SELECT * FROM ingredient WHERE name = :name COLLATE NOCASE)")
    suspend fun exists(name: String): Boolean


    @Query("UPDATE ingredient SET userDoesNotLike=:userDoesNotLike WHERE name = :name")
    suspend fun updateLike(name: String, userDoesNotLike: Boolean)

    @Delete
    suspend fun delete(ingredientEntity: IngredientEntity)

    @Query("SELECT * FROM ingredient WHERE name = :name")
    suspend fun get(name: String): IngredientEntity?

    @Query("SELECT * FROM ingredient")
    fun getAll(): Flow<List<IngredientEntity>>

}