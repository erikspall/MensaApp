package de.erikspall.mensaapp.data.sources.local.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProvider
import de.erikspall.mensaapp.data.sources.local.database.entities.Location
import de.erikspall.mensaapp.data.sources.local.database.entities.OpeningHours

data class FoodProviderWithoutMenus (
    @Embedded val foodProvider: FoodProvider,
    @Relation(
        parentColumn = "location_id",
        entityColumn = "lid"
    )
    val location: Location,
    @Relation(
        parentColumn = "fid",
        entityColumn = "food_provider_id"
    )
    val openingHours: List<OpeningHours>
)