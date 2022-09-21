package de.erikspall.mensaapp.data.sources.local.database.entities

import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import java.util.stream.Collectors

data class Meal (
    val name: String,
    val priceStudent: String,
    val priceGuest: String,
    val priceEmployee: String,
    val ingredients: List<Ingredient>,
    val allergens: List<Allergenic>
) {
    fun getPrice(role: Role): String{
        return when(role) {
            Role.STUDENT -> priceStudent
            Role.GUEST -> priceGuest
            Role.EMPLOYEE -> priceEmployee
            Role.INVALID -> "-1"
        }
    }

    fun getIngredientsAsString(): String {
        return ingredients.stream().map { it -> it.name }.collect(Collectors.joining(", "))
    }
}