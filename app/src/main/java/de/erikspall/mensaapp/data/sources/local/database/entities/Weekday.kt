package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekday")
data class Weekday (
    @PrimaryKey val wid: Long,
    val name: String
)