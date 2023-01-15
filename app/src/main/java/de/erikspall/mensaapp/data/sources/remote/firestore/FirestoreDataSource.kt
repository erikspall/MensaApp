package de.erikspall.mensaapp.data.sources.remote.firestore

import android.util.Log
import com.google.firebase.firestore.*
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.domain.utils.Extensions.toDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.*

class FirestoreDataSource(
    private val firestoreInstance: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchFoodProviders(
        source: Source = Source.CACHE,
        location: Location,
        category: Category
    ): Result<QuerySnapshot> = withContext(ioDispatcher) {
        try {
            Log.d("$TAG:fetchingProcess", "Data Source is using ${if (Source.CACHE == source) "CACHE" else "SERVER"}")
            val query = if (location == Location.ANY && category == Category.ANY)
                queryFoodProviders()
            else if (location == Location.ANY)
                queryFoodProvidersByCategory(category)
            else if (category == Category.ANY)
                queryFoodProvidersByLocation(location)
            else
                queryFoodProvidersByLocationAndCategory(location, category)

            val foodProviderSnapshot = query
                .get(source)
                .await()

            return@withContext Result.success(foodProviderSnapshot)

        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun fetchFoodProvider(
        source: Source = Source.CACHE,
        foodProviderId: Int
    ): Result<QuerySnapshot> = withContext(ioDispatcher) {
        try {
            val query = queryFoodProvidersById(foodProviderId)

            val foodProviderSnapshot = query
                .get(source)
                .await()

            return@withContext Result.success(foodProviderSnapshot)

        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun fetchAdditives(
        source: Source = Source.CACHE
    ): Result<QuerySnapshot> = withContext(ioDispatcher) {
        try {

            val additiveSnapshot = queryAdditives()
                .get(source)
                .await()

            return@withContext Result.success(additiveSnapshot)

        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun fetchMeals(
        source: Source = Source.CACHE,
        foodProviderId: Int,
        offset: Int
    ): Result<QuerySnapshot> = withContext(ioDispatcher) {
        try {

            val mealsSnapshot = queryMealsOfFoodProviderFromDate(
                foodProviderId,
                LocalDate.now().plusDays(offset.toLong()).toDate()
            )
                .get(source)
                .await()

            return@withContext Result.success(mealsSnapshot)

        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    private fun queryFoodProviders(): Query {
        return firestoreInstance.collection(COLLECTION_FOOD_PROVIDERS)
            .orderBy(FoodProvider.FIELD_NAME, Query.Direction.ASCENDING)
    }

    private fun queryFoodProvidersByLocation(location: Location): Query {
        return firestoreInstance.collection(COLLECTION_FOOD_PROVIDERS)
            .whereEqualTo(FoodProvider.FIELD_LOCATION, location.getRawValue())
            .orderBy(FoodProvider.FIELD_NAME, Query.Direction.ASCENDING)
    }

    private fun queryFoodProvidersByLocationAndCategory(location: Location, category: Category): Query {
        return firestoreInstance.collection(COLLECTION_FOOD_PROVIDERS)
            .whereEqualTo(FoodProvider.FIELD_LOCATION, location.getRawValue())
            .whereEqualTo(FoodProvider.FIELD_CATEGORY, category.getValue())
            .orderBy(FoodProvider.FIELD_NAME, Query.Direction.ASCENDING)
    }

    private fun queryFoodProvidersByCategory(category: Category): Query {
        return firestoreInstance.collection(COLLECTION_FOOD_PROVIDERS)
            .whereEqualTo(FoodProvider.FIELD_CATEGORY, category.getValue())
            .orderBy(FoodProvider.FIELD_NAME, Query.Direction.ASCENDING)
    }

    private fun queryFoodProvidersById(foodProviderId: Int): Query {
        return firestoreInstance.collection(COLLECTION_FOOD_PROVIDERS)
            .whereEqualTo(FoodProvider.FIELD_ID, foodProviderId)
    }

    private fun queryMealsOfFoodProviderStartingFromDate(foodProviderId: Int, date: Date): Query {
        return firestoreInstance.collectionGroup(COLLECTION_MENUS)
            .whereEqualTo(Meal.FIELD_FOOD_PROVIDER_ID, foodProviderId)
            .whereGreaterThanOrEqualTo(Meal.FIELD_DATE, date)
            .orderBy(Meal.FIELD_DATE, Query.Direction.ASCENDING)
    }

    private fun queryMealsOfFoodProviderFromDate(foodProviderId: Int, date: Date): Query {
        return firestoreInstance.collectionGroup(COLLECTION_MENUS)
            .whereEqualTo(Meal.FIELD_FOOD_PROVIDER_ID, foodProviderId)
            .whereEqualTo(Meal.FIELD_DATE, date)
            .orderBy(Meal.FIELD_DATE, Query.Direction.ASCENDING)
    }

    private fun queryAdditives(): Query {
        return firestoreInstance.collection(COLLECTION_ADDITIVES)
            .orderBy(Additive.FIELD_NAME, Query.Direction.ASCENDING)
    }

    companion object {
        const val COLLECTION_FOOD_PROVIDERS = "foodProviders"
        const val COLLECTION_MENUS = "menus"
        const val COLLECTION_ADDITIVES = "additives"

        const val TAG="FirestoreDataSource"
    }
}