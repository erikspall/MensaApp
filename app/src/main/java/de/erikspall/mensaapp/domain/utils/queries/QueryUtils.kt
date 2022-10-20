package de.erikspall.mensaapp.domain.utils.queries

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.domain.model.Menu
import java.util.*

object QueryUtils {
    // TODO: Find a way to inject the reference and dont construct it each time ...

    fun queryFoodProvidersByCategory(category: Category): Query {
        return FirebaseFirestore.getInstance().collection("foodProviders")
            .whereEqualTo(FoodProvider.FIELD_CATEGORY, category.getValue())
            .orderBy(FoodProvider.FIELD_NAME, Query.Direction.ASCENDING)
    }

    fun queryFoodProvidersById(foodProviderId: Int): Query {
        return FirebaseFirestore.getInstance().collection("foodProviders")
            .whereEqualTo(FoodProvider.FIELD_ID, foodProviderId)
    }

    fun queryMealsOfFoodProviderStartingFromDate(foodProviderId: Int, date: Date): Query {
        return FirebaseFirestore.getInstance().collectionGroup("menus")
            .whereEqualTo(Meal.FIELD_FOOD_PROVIDER_ID, foodProviderId)
            .whereGreaterThanOrEqualTo(Meal.FIELD_DATE, date)
            .orderBy(Meal.FIELD_DATE, Query.Direction.ASCENDING)
    }

    fun queryAdditives(): Query {
        return FirebaseFirestore.getInstance().collection("additives")
            .orderBy("name", Query.Direction.ASCENDING)
    }
}