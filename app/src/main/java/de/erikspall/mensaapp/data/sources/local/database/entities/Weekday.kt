package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekday")
data class Weekday (
    @PrimaryKey(autoGenerate = true) val wid: Long = 0,
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Weekday

        if (wid != other.wid) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = wid.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}