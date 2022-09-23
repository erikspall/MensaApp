package de.erikspall.mensaapp.data.sources.local.database.entities

interface MealComponent {
    fun getName(): String
    fun getUserDoesNotLike(): Boolean
}