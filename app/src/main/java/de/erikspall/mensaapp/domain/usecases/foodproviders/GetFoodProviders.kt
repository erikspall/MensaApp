package de.erikspall.mensaapp.domain.usecases.foodproviders

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.FoodProvider
import kotlinx.coroutines.Dispatchers

data class GetFoodProviders(
    private val context: Context,
    private val appRepository: AppRepository
) {
    operator fun invoke(
        location: Location,
        category: Category
    ): LiveData<OptionalResult<List<FoodProvider>>> = liveData(Dispatchers.IO) {
        emit(
            appRepository.getFoodProvidersFromFirestore(
                context.getString(location.getValue()),
                category.getValue()
            )
        )
    }

}
