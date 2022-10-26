package de.erikspall.mensaapp.data.sources.local.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.model.Additive

@Dao
interface AdditiveDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg additive: Additive)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(additive: Additive)

    @Update
    suspend fun updateAdditive(vararg additive: Additive)

    @Query("UPDATE additives SET isNotLiked=:userDoesNotLike WHERE name = :name AND type = :type")
    suspend fun updateLike(name: String, type: AdditiveType, userDoesNotLike: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM additives WHERE name = :name AND type = :type COLLATE NOCASE)")
    suspend fun exists(name: String, type: AdditiveType): Boolean

    @Delete
    suspend fun delete(additiveEntity: Additive)

    @Query("SELECT * FROM additives WHERE name = :name AND type = :type")
    suspend fun get(name: String, type: AdditiveType): Additive?

    @Query("SELECT * FROM additives")
    fun getAll(): LiveData<List<Additive>>
}