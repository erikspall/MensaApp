package de.erikspall.mensaapp.domain.usecases.foodprovider

import android.content.Context
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.Location
import de.erikspall.mensaapp.data.sources.local.database.relationships.FoodProviderWithoutMenus
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.FoodProviderOrder
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.streams.toList

class GetFoodProviders(
    private val repository: AppRepository,
) {
    operator fun invoke(
        foodProviderOrder: FoodProviderOrder = FoodProviderOrder.Name(OrderType.Descending),
        typeId: Long = -1L
    ): Flow<List<FoodProviderWithoutMenus>> {
        return repository.cachedProviders
            .map { rawFoodProviders ->
                val foodProviders = rawFoodProviders.filter { provider ->
                    provider.foodProvider.foodProviderTypeId == typeId
                }
            when (foodProviderOrder.orderType) {
                is OrderType.Ascending -> {
                    when (foodProviderOrder) {
                        is FoodProviderOrder.Name -> foodProviders.sortedBy { it.foodProvider.name.lowercase() }
                        is FoodProviderOrder.IsFavorite -> foodProviders.sortedBy { it.foodProvider.isFavorite }
                    }
                }
                is OrderType.Descending -> {
                    when (foodProviderOrder) {
                        is FoodProviderOrder.Name -> foodProviders.sortedByDescending { it.foodProvider.name.lowercase() }
                        is FoodProviderOrder.IsFavorite -> foodProviders.sortedByDescending { it.foodProvider.isFavorite }
                    }
                }
            }
        }
    }
}