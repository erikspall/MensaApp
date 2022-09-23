package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.IngredientDao
import de.erikspall.mensaapp.data.sources.local.database.entities.Allergenic
import de.erikspall.mensaapp.data.sources.local.database.entities.Ingredient
import kotlinx.coroutines.flow.Flow

class IngredientRepository(
        private val ingredientDao: IngredientDao
) {
    suspend fun insert(vararg ingredients: Ingredient) {
        ingredientDao.insertAll(*ingredients)
    }

    suspend fun insert(ingredient: Ingredient) {
        ingredientDao.insert(ingredient)
    }

    suspend fun exists(name: String): Boolean {
        return ingredientDao.exists(name)
    }

    suspend fun get(name: String): Ingredient? {
        return ingredientDao.get(name)
    }

    fun getAll(): Flow<List<Ingredient>> {
        return ingredientDao.getAll()
    }

    suspend fun delete(ingredient: Ingredient) {
        ingredientDao.delete(ingredient)
    }

    suspend fun update(vararg ingredients: Ingredient) {
        ingredientDao.updateIngredients(*ingredients)
    }
}