package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.FoodProviderTypeDao
import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProviderType

class FoodProviderTypeRepository(
    private val foodProviderTypeDao: FoodProviderTypeDao
) {
    suspend fun insert(vararg types: FoodProviderType) {
        foodProviderTypeDao.insertAll(*types)
    }

    suspend fun insert(type: FoodProviderType): Long {
        return foodProviderTypeDao.insert(type)
    }

    suspend fun exists(name: String): Boolean {
        return foodProviderTypeDao.exists(name)
    }

    suspend fun get(name: String): FoodProviderType? {
        return foodProviderTypeDao.get(name)
    }

    suspend fun delete(type: FoodProviderType) {
        foodProviderTypeDao.delete(type)
    }

    suspend fun update(vararg types: FoodProviderType) {
        foodProviderTypeDao.updateFoodProviderTypes(*types)
    }
}