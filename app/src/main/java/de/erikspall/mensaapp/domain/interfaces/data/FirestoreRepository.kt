package de.erikspall.mensaapp.domain.interfaces.data

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.domain.model.Menu
import java.time.LocalDate

interface FirestoreRepository {
    suspend fun fetchFoodProviders(
        location: Location,
        category: Category
    ): Result<List<FoodProvider>>

    suspend fun fetchFoodProvider(
        foodProviderId: Int
    ): Result<FoodProvider>

    suspend fun fetchAdditives(
    ): Result<List<Additive>>

    suspend fun fetchMeals(
        foodProviderId: Int,
        date: LocalDate
    ): Result<QuerySnapshot>

    suspend fun fetchData(
        request: Request
    ): Result<QuerySnapshot>
}