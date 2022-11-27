package de.erikspall.mensaapp.domain.interfaces.data

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Menu
import java.time.LocalDate

interface AppRepository {
    val allAllergens: LiveData<List<Additive>>
    val allIngredients: LiveData<List<Additive>>

    suspend fun fetchFoodProviders(
        location: Location,
        category: Category
    ): Result<List<FoodProvider>>

    suspend fun fetchFoodProvider(
        foodProviderId: Int
    ): Result<FoodProvider>

    /**
     * Does not return additives, they are saved in the local database instead (we want to persist
     * the users preference) - changes are brought to UI Layer by live data
     *
     * The method only returns OptionalResult to propagate errors
     */
    suspend fun fetchAllAdditives(
    ): Result<List<Additive>>

    suspend fun fetchMenus(
        foodProviderId: Int,
        date: LocalDate
    ): Result<List<Menu>>

    suspend fun setAdditiveLikeStatus(name: String, type: AdditiveType, userDoesNotLike: Boolean)
}