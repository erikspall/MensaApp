package de.erikspall.mensaapp.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import de.erikspall.mensaapp.data.sources.local.database.entities.AllergenEntity
import de.erikspall.mensaapp.data.sources.local.database.entities.IngredientEntity
import de.erikspall.mensaapp.domain.enums.Role

@IgnoreExtraProperties
data class Meal (
    var name: String = "",
    var allergenEntities: List<AllergenEntity> = emptyList(),
    var ingredientEntities: List<IngredientEntity> = emptyList(),
    var prices: Map<Role, String> = emptyMap()
) {
    companion object {
        const val FIELD_FOOD_PROVIDER_ID = "foodProviderId"
        const val FIELD_DATE = "date"
        const val FIELD_NAME = "name"
        const val FIELD_INGREDIENTS = "ingredients"
        const val FIELD_ALLERGENS = "allergens"
        const val FIELD_PRICE_EMPLOYEE = "priceEmployee"
        const val FIELD_PRICE_GUEST = "priceGuest"
        const val FIELD_PRICE_STUDENT = "priceStudent"

    }
}