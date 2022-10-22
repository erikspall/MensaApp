package de.erikspall.mensaapp.domain.usecases.foodproviders

import com.google.firebase.firestore.Source
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.model.Menu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class FetchMenus(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(
        foodProviderId: Int,
        date: LocalDate
    ): OptionalResult<List<Menu>> {
        return appRepository.fetchMenus(
            foodProviderId = foodProviderId,
            date = date
        )
    }
}