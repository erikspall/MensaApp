package de.erikspall.mensaapp.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.utils.Extensions.equalsIgnoreOrder

@IgnoreExtraProperties
data class Meal(
    var name: String = "",
    var additives: List<Additive> = emptyList(),
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Meal

        if (name != other.name) return false
        if (!additives.equalsIgnoreOrder(other.additives)) return false
        if (prices[Role.GUEST] != other.prices[Role.GUEST] ||
            prices[Role.STUDENT] != other.prices[Role.STUDENT] ||
            prices[Role.EMPLOYEE] != other.prices[Role.EMPLOYEE]
        ) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + additives.hashCode()
        result = 31 * result + prices.hashCode()
        return result
    }
}