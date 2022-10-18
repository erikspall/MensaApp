package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.room.*
import de.erikspall.mensaapp.data.sources.local.database.entities.AllergenicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AllergenicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg allergenicEntity: AllergenicEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(allergenicEntity: AllergenicEntity)

    @Update
    suspend fun updateAllergenic(vararg allergenicEntity: AllergenicEntity)

    @Query("UPDATE allergenic SET userDoesNotLike=:userDoesNotLike WHERE name = :name")
    suspend fun updateLike(name: String, userDoesNotLike: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM allergenic WHERE name = :name COLLATE NOCASE)")
    suspend fun exists(name: String): Boolean

    @Delete
    suspend fun delete(allergenicEntity: AllergenicEntity)

    @Query("SELECT * FROM allergenic WHERE name = :name")
    suspend fun get(name: String): AllergenicEntity?

    @Query("SELECT * FROM allergenic")
    fun getAll(): Flow<List<AllergenicEntity>>
}