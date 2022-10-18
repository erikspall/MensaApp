package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.AllergenicDao
import de.erikspall.mensaapp.data.sources.local.database.entities.AllergenicEntity
import kotlinx.coroutines.flow.Flow

class AllergenicRepository(
        private val allergenicDao: AllergenicDao
) {
    suspend fun insert(vararg allergenicEntity: AllergenicEntity) {
        allergenicDao.insertAll(*allergenicEntity)
    }

    suspend fun insert(allergenicEntity: AllergenicEntity) {
        allergenicDao.insert(allergenicEntity)
    }

    suspend fun exists(name: String): Boolean {
        return allergenicDao.exists(name)
    }

    suspend fun get(name: String): AllergenicEntity? {
        return allergenicDao.get(name)
    }

    suspend fun updateLike(name: String, userDoesNotLike: Boolean) {
        allergenicDao.updateLike(name, userDoesNotLike)
    }

    fun getAll(): Flow<List<AllergenicEntity>> {
        return allergenicDao.getAll()
    }

    suspend fun delete(allergenicEntity: AllergenicEntity) {
        allergenicDao.delete(allergenicEntity)
    }

    suspend fun update(vararg allergenicEntity: AllergenicEntity) {
        allergenicDao.updateAllergenic(*allergenicEntity)
    }
}