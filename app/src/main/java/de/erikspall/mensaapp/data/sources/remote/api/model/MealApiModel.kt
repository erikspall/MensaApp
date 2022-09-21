package de.erikspall.mensaapp.data.sources.remote.api.model

data class MealApiModel (
    val priceStudent: String,
    val name: String,
    val priceEmployee: String,
    val priceGuest: String,
    val ingredients: String,
    val id: Int,
    val allergens: String
)
