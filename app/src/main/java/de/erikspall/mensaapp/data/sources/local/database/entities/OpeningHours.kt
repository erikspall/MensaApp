package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "opening_hours")
data class OpeningHours (
    @PrimaryKey val oid: Long,
    @ColumnInfo(name = "food_provider_id") val foodProviderId: Long,
    @ColumnInfo(name = "weekday_id") val weekdayId: Long,
    @ColumnInfo(name = "opens_at") val opensAt: String,
    @ColumnInfo(name = "closes_at") val closesAt: String,
    @ColumnInfo(name = "get_food_till") val getFoodTill: String,
    val opened: Boolean
)