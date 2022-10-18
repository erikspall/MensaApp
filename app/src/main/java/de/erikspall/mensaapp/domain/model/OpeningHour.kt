package de.erikspall.mensaapp.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.time.DayOfWeek

data class OpeningHour (
    var closesAt: String,
    var dayOfWeek: DayOfWeek,
    var getFoodTill: String,
    var isOpen: Boolean,
    var opensAt: String
) {
    companion object{
        const val FIELD_CLOSES_AT = "closesAt"
        const val FIELD_GET_FOOD_TILL = "getAMealTill"
        const val FIELD_IS_OPEN = "isOpen"
        const val FIELD_OPENS_AT = "opensAt"
    }
}