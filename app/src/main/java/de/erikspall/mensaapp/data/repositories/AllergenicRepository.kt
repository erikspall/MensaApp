package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.AllergenicDao
import de.erikspall.mensaapp.data.sources.local.database.entities.Allergenic
import de.erikspall.mensaapp.data.sources.local.database.entities.Location
import kotlinx.coroutines.flow.Flow

class AllergenicRepository(
        private val allergenicDao: AllergenicDao
) {
    suspend fun insert(vararg allergenic: Allergenic) {
        allergenicDao.insertAll(*allergenic)
    }

    suspend fun insert(allergenic: Allergenic) {
        allergenicDao.insert(allergenic)
    }

    suspend fun exists(name: String): Boolean {
        return allergenicDao.exists(name)
    }

    suspend fun get(name: String): Allergenic? {
        return allergenicDao.get(name)
    }

    suspend fun updateLike(name: String, userDoesNotLike: Boolean) {
        allergenicDao.updateLike(name, userDoesNotLike)
    }

    fun getAll(): Flow<List<Allergenic>> {
        return allergenicDao.getAll()
    }

    suspend fun delete(allergenic: Allergenic) {
        allergenicDao.delete(allergenic)
    }

    suspend fun update(vararg allergenic: Allergenic) {
        allergenicDao.updateAllergenic(*allergenic)
    }
}