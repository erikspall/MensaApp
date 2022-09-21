package de.erikspall.mensaapp.data.sources.local.database.entities

import java.util.stream.Collector
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
        }
    }

    fun getIngredientsAsString(): String {
        return ingredients.stream().map { it -> it.name }.collect(Collectors.joining(", "))
    }
}