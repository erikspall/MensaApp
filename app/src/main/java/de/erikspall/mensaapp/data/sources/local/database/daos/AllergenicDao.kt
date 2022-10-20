package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import de.erikspall.mensaapp.data.sources.local.database.entities.AllergenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AllergenicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg allergenEntity: AllergenEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(allergenEntity: AllergenEntity)

    @Update
    suspend fun updateAllergenic(vararg allergenEntity: AllergenEntity)

    @Query("UPDATE allergenic SET userDoesNotLike=:userDoesNotLike WHERE name = :name")
    suspend fun updateLike(name: String, userDoesNotLike: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM allergenic WHERE name = :name COLLATE NOCASE)")
    suspend fun exists(name: String): Boolean

    @Delete
    suspend fun delete(allergenEntity: AllergenEntity)

    @Query("SELECT * FROM allergenic WHERE name = :name")
    suspend fun get(name: String): AllergenEntity?

    @Query("SELECT * FROM allergenic")
    fun getAll(): LiveData<List<AllergenEntity>>
}