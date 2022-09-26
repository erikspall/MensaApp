package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.room.*
import de.erikspall.mensaapp.data.sources.local.database.entities.Ingredient
import de.erikspall.mensaapp.data.sources.local.database.entities.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg ingredients: Ingredient)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ingredient: Ingredient)

    @Update
    suspend fun updateIngredients(vararg ingredients: Ingredient)

    @Query("SELECT EXISTS(SELECT * FROM ingredient WHERE name = :name COLLATE NOCASE)")
    suspend fun exists(name: String): Boolean


    @Query("UPDATE ingredient SET userDoesNotLike=:userDoesNotLike WHERE name = :name")
    suspend fun updateLike(name: String, userDoesNotLike: Boolean)

    @Delete
    suspend fun delete(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient WHERE name = :name")
    suspend fun get(name: String): Ingredient?

    @Query("SELECT * FROM ingredient")
    fun getAll(): Flow<List<Ingredient>>

}