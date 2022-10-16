package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.FoodProviderDao
import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProvider
import de.erikspall.mensaapp.data.sources.local.database.relationships.FoodProviderWithInfo
import de.erikspall.mensaapp.data.sources.local.database.relationships.FoodProviderWithoutMenus
import kotlinx.coroutines.flow.Flow

class FoodProviderRepository(
    private val foodProviderDao: FoodProviderDao
) {
    suspend fun insert(vararg foodProviders: FoodProvider) {
        foodProviderDao.insertAll(*foodProviders)
    }

    suspend fun insert(foodProvider: FoodProvider): Long {
        return foodProviderDao.insert(foodProvider)
    }

    suspend fun delete(foodProvider: FoodProvider) {
        foodProviderDao.delete(foodProvider)
    }

    suspend fun exists(fid: Long): Boolean {
        return foodProviderDao.exists(fid)
    }

    suspend fun get(fid: Int): FoodProvider? {
        return foodProviderDao.get(fid)
    }

    suspend fun update(vararg foodProviders: FoodProvider) {
        foodProviderDao.updateFoodProviders(*foodProviders)
    }

    fun getFoodProvidersWithoutMenus(): Flow<List<FoodProviderWithoutMenus>> =
        foodProviderDao.getFoodProvidersWithoutMenus()

    fun getFoodProviderWithInfo(fid: Long): Flow<FoodProviderWithInfo> {
        return foodProviderDao.getFoodProvidersWithInfo(fid)
    }

    fun getFoodProvidersByTypeAndLocation(tid: Long, lid: Long): Flow<List<FoodProviderWithoutMenus>> {
        return foodProviderDao.getFoodProvidersByTypeAndLocation(tid, lid)
    }
}