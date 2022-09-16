package de.erikspall.mensaapp.data.sources.local.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProvider
import de.erikspall.mensaapp.data.sources.local.database.entities.Location

data class FoodProviderWithoutMenus (
    @Embedded val foodProvider: FoodProvider,
    @Relation(
        parentColumn = "location_id",
        entityColumn = "lid"
    )
    val location: Location
)