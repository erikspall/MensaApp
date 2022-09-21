package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_provider_types",
    indices = [Index(value = ["name"], unique = true)]
)
data class FoodProviderType(
    @PrimaryKey(autoGenerate = true)
    val tid: Long = 0,

    @ColumnInfo()
    val name: String
)