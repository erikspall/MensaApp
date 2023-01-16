package de.erikspall.mensaapp.domain.usecases.foodproviders

import de.erikspall.mensaapp.domain.interfaces.data.AppRepository
import de.erikspall.mensaapp.domain.model.Menu

class FetchMenu(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(
        foodProviderId: Int,
        offset: Int
    ): Result<Menu> {
        return appRepository.fetchMenu(
            foodProviderId = foodProviderId,
            offset = offset
        )
    }
}