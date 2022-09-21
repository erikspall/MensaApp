package de.erikspall.mensaapp.data.sources.remote.api.model

data class MenuApiModel (
    val date: String,
    val meals: List<MealApiModel>
)
