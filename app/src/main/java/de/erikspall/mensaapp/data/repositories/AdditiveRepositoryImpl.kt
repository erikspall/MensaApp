package de.erikspall.mensaapp.data.repositories

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.repositories.interfaces.AdditiveRepository
import de.erikspall.mensaapp.data.sources.local.database.daos.AdditiveDao
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.model.Additive


class AdditiveRepositoryImpl(
        private val additiveDao: AdditiveDao
) : AdditiveRepository {
    override suspend fun insert(vararg additives: Additive) {
        additiveDao.insertAll(*additives)
    }

    override suspend fun insert(additive: Additive) {
        additiveDao.insert(additive)
    }

    override suspend fun exists(name: String, type: AdditiveType): Boolean {
        return additiveDao.exists(name, type)
    }

    override suspend fun get(name: String, type: AdditiveType): Result<Additive> {
        val daoRes = additiveDao.get(name, type)
        return if (daoRes != null)
            Result.success(daoRes)
        else
            Result.failure(NoSuchElementException("No additive found with name '$name' and type '$type'"))
    }

    override suspend fun updateLike(name: String, type: AdditiveType, isNotLiked: Boolean) {
        additiveDao.updateLike(name, type, isNotLiked)
    }

    override fun getAll(): LiveData<List<Additive>> {
        return additiveDao.getAll()
    }

    override fun getAll(additiveType: AdditiveType): LiveData<List<Additive>> {
        return additiveDao.getAll(additiveType)
    }

    override suspend fun delete(additive: Additive) {
        additiveDao.delete(additive)
    }

    override suspend fun update(vararg additives: Additive) {
        additiveDao.updateAdditive(*additives)
    }

    override suspend fun getOrInsertAllAdditives(rawAdditives: String, type: AdditiveType): List<Additive> {
        val additives = mutableListOf<Additive>()
        for (rawAdditive in rawAdditives.split(",")) {
            additives.add(getOrInsertAdditive(rawAdditive, type))
        }
        return additives
    }

    override suspend fun getOrInsertAdditive(name: String, type: AdditiveType): Additive {
        return if (exists(name, type)) {
            get(name, type).getOrThrow()
        } else {
            val additive = Additive(
                name = name,
                icon = R.drawable.ic_mensa, // TODO: extract icon
                type = type
            )
            if (additive.name.isNotBlank()) // TODO: find a better way if meal cannot be parsed
                insert(additive)

            /* return */ additive
        }
    }
}