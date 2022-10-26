package de.erikspall.mensaapp.data.repositories.interfaces

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.sources.local.database.entities.AdditiveEntity
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Menu
import java.time.LocalDate

interface AppRepository {
    val allAllergens: LiveData<List<AdditiveEntity>>
    val allIngredients: LiveData<List<AdditiveEntity>>

    suspend fun fetchFoodProviders(
        location: Location,
        category: Category
    ): OptionalResult<List<FoodProvider>>

    suspend fun fetchFoodProvider(
        foodProviderId: Int
    ): OptionalResult<FoodProvider>

    /**
     * Does not return additives, they are saved in the local database instead (we want to persist
     * the users preference) - changes are brought to UI Layer by live data
     *
     * The method only returns OptionalResult to propagate errors
     */
    suspend fun fetchAllAdditives(
    ): OptionalResult<List<Additive>>

    suspend fun fetchMenus(
        foodProviderId: Int,
        date: LocalDate
    ): OptionalResult<List<Menu>>

    suspend fun setAdditiveLikeStatus(name: String, type: AdditiveType, userDoesNotLike: Boolean)
}