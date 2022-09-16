package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location (
    @PrimaryKey val lid: Long,
    val name: String
)