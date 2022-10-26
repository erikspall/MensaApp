package de.erikspall.mensaapp.data.repositories

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.data.repositories.interfaces.AdditiveRepository
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.model.Additive

class FakeAdditiveRepository: AdditiveRepository {


    override suspend fun insert(vararg additive: Additive) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(additive: Additive) {
        TODO("Not yet implemented")
    }

    override suspend fun exists(name: String, type: AdditiveType): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(name: String, type: AdditiveType): Additive? {
        TODO("Not yet implemented")
    }

    override suspend fun updateLike(name: String, type: AdditiveType, userDoesNotLike: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getAll(): LiveData<List<Additive>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(additive: Additive) {
        TODO("Not yet implemented")
    }

    override suspend fun update(vararg additive: Additive) {
        TODO("Not yet implemented")
    }

    override suspend fun getOrInsertAllAdditives(
        rawAdditives: String,
        type: AdditiveType
    ): List<Additive> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrInsertAdditive(name: String, type: AdditiveType): Additive {
        TODO("Not yet implemented")
    }
}