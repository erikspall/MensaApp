package de.erikspall.mensaapp.domain.usecases.foodproviders

import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.repositories.AppRepositoryImpl
import de.erikspall.mensaapp.data.repositories.interfaces.AppRepository
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.FoodProvider

data class FetchFoodProviders(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(
        location: Location,
        category: Category
    ): OptionalResult<List<FoodProvider>> {
        return appRepository.fetchFoodProviders(
            location = location,
            category = category
        )
    }

}
