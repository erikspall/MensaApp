package de.erikspall.mensaapp.domain.usecases.foodproviders

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.firestore.Source
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.FoodProvider
import kotlinx.coroutines.Dispatchers

data class GetFoodProviders(
    private val appRepository: AppRepository
) {
    operator fun invoke(
        category: Category,
        source: Source = Source.CACHE
    ): LiveData<OptionalResult<List<FoodProvider>>> = liveData(Dispatchers.IO) {
        emit(
            appRepository.getFoodProvidersFromFirestore(
                category.getValue(),
                source
            )
        )
    }

}
