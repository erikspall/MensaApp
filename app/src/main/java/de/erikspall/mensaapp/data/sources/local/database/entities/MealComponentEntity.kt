package de.erikspall.mensaapp.data.sources.local.database.entities

interface MealComponentEntity {
    fun getName(): String
    fun getUserDoesNotLike(): Boolean
}