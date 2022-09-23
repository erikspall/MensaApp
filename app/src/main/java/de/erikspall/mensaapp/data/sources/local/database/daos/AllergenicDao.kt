package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.room.*
import de.erikspall.mensaapp.data.sources.local.database.entities.Allergenic
import kotlinx.coroutines.flow.Flow

@Dao
interface AllergenicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg allergenic: Allergenic)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(allergenic: Allergenic)

    @Update
    suspend fun updateAllergenic(vararg allergenic: Allergenic)

    @Query("SELECT EXISTS(SELECT * FROM allergenic WHERE name = :name COLLATE NOCASE)")
    suspend fun exists(name: String): Boolean

    @Delete
    suspend fun delete(allergenic: Allergenic)

    @Query("SELECT * FROM allergenic WHERE name = :name")
    suspend fun get(name: String): Allergenic?

    @Query("SELECT * FROM allergenic")
    fun getAll(): Flow<List<Allergenic>>
}