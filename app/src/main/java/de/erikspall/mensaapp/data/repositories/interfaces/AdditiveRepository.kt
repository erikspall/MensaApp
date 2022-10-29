package de.erikspall.mensaapp.data.repositories.interfaces

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.model.Additive

interface AdditiveRepository {
    suspend fun insert(vararg additives: Additive)

    suspend fun insert(additive: Additive)

    suspend fun exists(name: String, type: AdditiveType): Boolean

    suspend fun get(name: String, type: AdditiveType): Result<Additive>

    suspend fun updateLike(name: String, type: AdditiveType, isNotLiked: Boolean)
    fun getAll(): LiveData<List<Additive>>

    suspend fun delete(additive: Additive)

    suspend fun update(vararg additives: Additive)

    suspend fun getOrInsertAllAdditives(rawAdditives: String, type: AdditiveType): List<Additive>

    suspend fun getOrInsertAdditive(name: String, type: AdditiveType): Additive
}