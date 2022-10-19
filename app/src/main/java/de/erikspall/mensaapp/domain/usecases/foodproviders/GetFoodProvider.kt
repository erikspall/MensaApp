package de.erikspall.mensaapp.domain.usecases.foodproviders

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.firestore.Source
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.utils.queries.QueryUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFoodProvider(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(
        foodProviderId: Int,
        source: Source = Source.CACHE
    ): OptionalResult<FoodProvider> = withContext(Dispatchers.IO) {
        val temp = appRepository.getFoodProvidersFromFirestore(
            source,
            QueryUtils.queryFoodProvidersById(foodProviderId)
        )
        if (temp.isPresent) {
            return@withContext OptionalResult.of(
                temp.get().first { foodProvider -> foodProvider.id == foodProviderId }
            )
        } else {
            return@withContext OptionalResult.ofMsg(temp.getMessage())
        }
    }
}