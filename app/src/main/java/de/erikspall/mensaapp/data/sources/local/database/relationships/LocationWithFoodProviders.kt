package de.erikspall.mensaapp.data.sources.local.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProvider
import de.erikspall.mensaapp.data.sources.local.database.entities.Location

data class LocationWithFoodProviders(
    @Embedded val location: Location,
    @Relation(
        parentColumn = "lid",
        entityColumn = "location_id"
    )
    val foodProviders: List<FoodProvider>
)
