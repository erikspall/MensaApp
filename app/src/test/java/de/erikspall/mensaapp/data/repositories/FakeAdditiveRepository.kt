package de.erikspall.mensaapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.repositories.interfaces.AdditiveRepository
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.model.Additive

class FakeAdditiveRepository(
    var additives: MutableList<Additive> = mutableListOf()
) : AdditiveRepository {


    override suspend fun insert(vararg additives: Additive) {
        this.additives.addAll(additives)
    }

    override suspend fun insert(additive: Additive) {
        additives.add(additive)
    }

    override suspend fun exists(name: String, type: AdditiveType): Boolean {
        return additives.any { additive -> additive.name == name && additive.type == type }
    }

    override suspend fun get(name: String, type: AdditiveType): Result<Additive> {
        val res: Additive

        try {
            res = additives.first { additive -> additive.name == name && additive.type == type }
        } catch (e: Exception) {
            return Result.failure(NoSuchElementException("No additive found with name '$name' and type '$type'"))
        }

        return Result.success(res)
    }

    override suspend fun updateLike(name: String, type: AdditiveType, isNotLiked: Boolean) {
        val index =
            additives.indexOf(additives.find { additive -> additive.name == name && additive.type == type })
        if (index != -1) {
            val old = additives[index]
            val new = Additive(
                name = old.name,
                type = old.type,
                isNotLiked = isNotLiked,
                icon = old.icon
            )
            additives[index] = new
        }
    }

    override fun getAll(): LiveData<List<Additive>> {
        return MutableLiveData(additives)
    }

    override suspend fun delete(additive: Additive) {
        val index =
            additives.indexOf(additives.find { additiveInList -> additiveInList.name == additive.name && additiveInList.type == additive.type })
        if (index != -1)
            additives.removeAt(index)
    }

    override suspend fun update(vararg additives: Additive) {
        for (additive in additives) {
            val index =
                this.additives.indexOf(this.additives.find { additiveInList -> additiveInList.name == additive.name && additiveInList.type == additive.type })

            if (index != -1) {
                this.additives.removeAt(index)
                this.additives.add(index, additive)
            }
        }

    }

    override suspend fun getOrInsertAllAdditives(
        rawAdditives: String,
        type: AdditiveType
    ): List<Additive> {
        val split = rawAdditives.split(",")
        val resList = mutableListOf<Additive>()
        for (raw in split) {
            resList.add(getOrInsertAdditive(raw, type))
        }
        return resList
    }

    override suspend fun getOrInsertAdditive(name: String, type: AdditiveType): Additive {
        val getResult = get(name, type)
        return if (getResult.isFailure) {
            val ad = Additive(name = name, type = type)
            insert(ad)

            additives.last()

        } else {
            getResult.getOrThrow()
        }
    }
}