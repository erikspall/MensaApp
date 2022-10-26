package de.erikspall.mensaapp.data.repositories.interfaces

import com.google.firebase.firestore.QuerySnapshot
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider
import java.time.LocalDate

interface FirestoreRepository {
    suspend fun fetchFoodProviders(
        location: Location,
        category: Category
    ): OptionalResult<List<FoodProvider>>

    suspend fun fetchFoodProvider(
        foodProviderId: Int
    ): OptionalResult<FoodProvider>

    suspend fun fetchAdditives(
    ): OptionalResult<List<Additive>>

    suspend fun fetchMeals(
        foodProviderId: Int,
        date: LocalDate
    ): OptionalResult<QuerySnapshot>
}