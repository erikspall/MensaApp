package de.erikspall.mensaapp.domain.utils.queries

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.erikspall.mensaapp.domain.model.FoodProvider

object QueryUtils {
    fun queryFoodProvidersByLocationAndCategory(location: String, category: String): Query {
        return Firebase.firestore.collection("foodProviders")
            .whereEqualTo(FoodProvider.FIELD_LOCATION, location)
            .whereEqualTo(FoodProvider.FIELD_CATEGORY, category)
    }
}