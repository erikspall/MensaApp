package de.erikspall.mensaapp.domain.utils.queries

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.model.FoodProvider
import javax.inject.Inject

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
}