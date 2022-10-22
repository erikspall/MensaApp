package de.erikspall.mensaapp.data.sources.remote.firestore

import com.google.firebase.firestore.*
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Meal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class FirestoreDataSource(
    private val firestoreInstance: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchFoodProviders(
        source: Source,
        location: Location,
        category: Category
    ): OptionalResult<QuerySnapshot> = withContext(ioDispatcher) {
        try {
            val query = if (location == Location.ANY && category == Category.ANY)
                queryFoodProviders()
            else if (location == Location.ANY)
                queryFoodProvidersByCategory(category)
            else if (category == Category.ANY)
                queryFoodProvidersByLocation(location)
            else
                queryFoodProvidersByLocationAndCategory(location, category)

            val foodProviderSnapshot = getSnapshot(
                source,
                query
            )

            return@withContext OptionalResult.of(foodProviderSnapshot)

        } catch (e: FirebaseFirestoreException) {
            return@withContext OptionalResult.ofMsg(e.message ?: "An unknown error occured")
        } catch (e: Exception) {
            return@withContext OptionalResult.ofMsg(e.message ?: "An unknown error occured")
        }
    }

    suspend fun fetchAdditives(
        source: Source
    ): OptionalResult<QuerySnapshot> = withContext(ioDispatcher) {
        try {

            val additiveSnapshot = getSnapshot(
                source,
                queryAdditives()
            )

            return@withContext OptionalResult.of(additiveSnapshot)

        } catch (e: FirebaseFirestoreException) {
            return@withContext OptionalResult.ofMsg(e.message ?: "An unknown error occured")
        } catch (e: Exception) {
            return@withContext OptionalResult.ofMsg(e.message ?: "An unknown error occured")
        }
    }

    suspend fun fetchMeals(
        source: Source,
        foodProviderId: Int,
        date: Date
    ): OptionalResult<QuerySnapshot> = withContext(ioDispatcher) {
        try {

            val mealsSnapshot = getSnapshot(
                source,
                queryMealsOfFoodProviderStartingFromDate(foodProviderId, date)
            )

            return@withContext OptionalResult.of(mealsSnapshot)

        } catch (e: FirebaseFirestoreException) {
            return@withContext OptionalResult.ofMsg(e.message ?: "An unknown error occured")
        } catch (e: Exception) {
            return@withContext OptionalResult.ofMsg(e.message ?: "An unknown error occured")
        }
    }

    private suspend fun getSnapshot(
        source: Source,
        query: Query
    ): QuerySnapshot {
        val backupSource = if (source == Source.CACHE) Source.SERVER else Source.CACHE

        var snapshot = query
            .get(source)
            .await()

        if (snapshot.isEmpty)
            snapshot = query
                .get(backupSource)
                .await()

        return snapshot
    }

    private fun queryFoodProviders(): Query {
        return firestoreInstance.collection(COLLECTION_FOOD_PROVIDERS)
            .orderBy(FoodProvider.FIELD_NAME, Query.Direction.ASCENDING)
    }

    private fun queryFoodProvidersByLocation(location: Location): Query {
        return firestoreInstance.collection(COLLECTION_FOOD_PROVIDERS)
            .whereEqualTo(FoodProvider.FIELD_LOCATION, location.getValue())
            .orderBy(FoodProvider.FIELD_NAME, Query.Direction.ASCENDING)
    }

    private fun queryFoodProvidersByLocationAndCategory(location: Location, category: Category): Query {
        return firestoreInstance.collection(COLLECTION_FOOD_PROVIDERS)
            .whereEqualTo(FoodProvider.FIELD_LOCATION, location.getValue())
            .whereEqualTo(FoodProvider.FIELD_CATEGORY, category.getValue())
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

    private fun queryAdditives(): Query {
        return firestoreInstance.collection(COLLECTION_ADDITIVES)
            .orderBy(Additive.FIELD_NAME, Query.Direction.ASCENDING)
    }

    companion object {
        const val COLLECTION_FOOD_PROVIDERS = "foodProviders"
        const val COLLECTION_MENUS = "menus"
        const val COLLECTION_ADDITIVES = "additives"
    }
}