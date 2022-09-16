package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location (
    @PrimaryKey val lid: Long,
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (lid != other.lid) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lid.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}