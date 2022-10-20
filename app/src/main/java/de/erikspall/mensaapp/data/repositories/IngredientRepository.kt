package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.IngredientDao
import de.erikspall.mensaapp.data.sources.local.database.entities.IngredientEntity
import kotlinx.coroutines.flow.Flow

class IngredientRepository(
        private val ingredientDao: IngredientDao
) {
    suspend fun insert(vararg ingredientEntities: IngredientEntity) {
        ingredientDao.insertAll(*ingredientEntities)
    }

    suspend fun insert(ingredientEntity: IngredientEntity) {
        ingredientDao.insert(ingredientEntity)
    }

    suspend fun exists(name: String): Boolean {
        return ingredientDao.exists(name)
    }

    suspend fun get(name: String): IngredientEntity? {
        return ingredientDao.get(name)
    }

    suspend fun updateLike(name: String, userDoesNotLike: Boolean) {
        ingredientDao.updateLike(name, userDoesNotLike)
    }

    fun getAll(): Flow<List<IngredientEntity>> {
        return ingredientDao.getAll()
    }

    suspend fun delete(ingredientEntity: IngredientEntity) {
        ingredientDao.delete(ingredientEntity)
    }

    suspend fun update(vararg ingredientEntities: IngredientEntity) {
        ingredientDao.updateIngredients(*ingredientEntities)
    }
}