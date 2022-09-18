package de.erikspall.mensaapp.domain.usecases.foodprovider

import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.relationships.FoodProviderWithInfo
import kotlinx.coroutines.flow.Flow

class GetInfoOfFoodProvider(
    private val repository: AppRepository
) {
    operator fun invoke(fid: Long): Flow<FoodProviderWithInfo> {
        return repository.getFoodProviderWithInfo(fid)
    }
}