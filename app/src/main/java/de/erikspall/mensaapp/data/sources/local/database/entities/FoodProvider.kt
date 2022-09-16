package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)],
tableName = "food_providers")
data class FoodProvider (
    @PrimaryKey val fid: Long,
    val name: String,
    @ColumnInfo(name = "location_id") val locationId: Long,
    //@ColumnInfo(name = "opening_hours_id") val openingHoursId: Int,
    val info: String,
    @ColumnInfo(name = "additional_info") val additionalInfo: String
)