package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "opening_hours")
data class OpeningHours (
    @PrimaryKey(autoGenerate = true) val oid: Long = 0,
    @ColumnInfo(name = "food_provider_id") val foodProviderId: Long,
    @ColumnInfo(name = "weekday_id") val weekdayId: Long,
    @ColumnInfo(name = "opens_at") val opensAt: String,
    @ColumnInfo(name = "closes_at") val closesAt: String,
    //@ColumnInfo(name = "get_food_till") val getFoodTill: String,
    val opened: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OpeningHours

        if (oid != other.oid) return false
        if (foodProviderId != other.foodProviderId) return false
        if (weekdayId != other.weekdayId) return false
        if (opensAt != other.opensAt) return false
        if (closesAt != other.closesAt) return false
        ////if (getFoodTill != other.getFoodTill) return false
        if (opened != other.opened) return false

        return true
    }

    override fun hashCode(): Int {
        var result = oid.hashCode()
        result = 31 * result + foodProviderId.hashCode()
        result = 31 * result + weekdayId.hashCode()
        result = 31 * result + opensAt.hashCode()
        result = 31 * result + closesAt.hashCode()
       // result = 31 * result + getFoodTill.hashCode()
        result = 31 * result + opened.hashCode()
        return result
    }
}