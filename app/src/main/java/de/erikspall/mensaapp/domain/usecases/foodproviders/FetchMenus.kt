package de.erikspall.mensaapp.domain.usecases.foodproviders

import de.erikspall.mensaapp.data.repositories.interfaces.AppRepository
import de.erikspall.mensaapp.domain.model.Menu
import java.time.LocalDate

class FetchMenus(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(
        foodProviderId: Int,
        date: LocalDate
    ): Result<List<Menu>> {
        return appRepository.fetchMenus(
            foodProviderId = foodProviderId,
            date = date
        )
    }
}