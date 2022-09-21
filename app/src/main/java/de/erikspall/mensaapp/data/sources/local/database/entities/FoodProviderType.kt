package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_provider_types")
data class FoodProviderType (
    @PrimaryKey
    val tid: Long = 0,
    val name: String
)