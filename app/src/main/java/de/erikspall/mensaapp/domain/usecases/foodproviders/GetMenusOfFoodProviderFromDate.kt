package de.erikspall.mensaapp.domain.usecases.foodproviders

import com.google.firebase.firestore.Source
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Menu
import de.erikspall.mensaapp.domain.utils.queries.QueryUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class GetMenusOfFoodProviderFromDate(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(
        foodProviderId: Int,
        date: LocalDate,
        source: Source = Source.CACHE
    ): OptionalResult<List<Menu>> = withContext(Dispatchers.IO) {
        return@withContext appRepository.getMenusOfFoodProvider(
            foodProviderId = foodProviderId,
            date = date,
            source = source
        )
    }
}