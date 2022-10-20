package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.AllergenicDao
import de.erikspall.mensaapp.data.sources.local.database.entities.AllergenEntity
import kotlinx.coroutines.flow.Flow

class AllergenicRepository(
        private val allergenicDao: AllergenicDao
) {
    suspend fun insert(vararg allergenEntity: AllergenEntity) {
        allergenicDao.insertAll(*allergenEntity)
    }

    suspend fun insert(allergenEntity: AllergenEntity) {
        allergenicDao.insert(allergenEntity)
    }

    suspend fun exists(name: String): Boolean {
        return allergenicDao.exists(name)
    }

    suspend fun get(name: String): AllergenEntity? {
        return allergenicDao.get(name)
    }

    suspend fun updateLike(name: String, userDoesNotLike: Boolean) {
        allergenicDao.updateLike(name, userDoesNotLike)
    }

    fun getAll(): Flow<List<AllergenEntity>> {
        return allergenicDao.getAll()
    }

    suspend fun delete(allergenEntity: AllergenEntity) {
        allergenicDao.delete(allergenEntity)
    }

    suspend fun update(vararg allergenEntity: AllergenEntity) {
        allergenicDao.updateAllergenic(*allergenEntity)
    }
}