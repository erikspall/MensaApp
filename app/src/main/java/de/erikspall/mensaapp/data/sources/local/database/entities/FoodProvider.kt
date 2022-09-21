package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "food_providers")
data class FoodProvider (
    @PrimaryKey val fid: Long,
    val name: String,
    @ColumnInfo(name = "food_provider_type_id") val foodProviderTypeId: Long,
    @ColumnInfo(name = "location_id") val locationId: Long,
    @ColumnInfo(name = "type") val type: String,
    //@ColumnInfo(name = "opening_hours_id") val openingHoursId: Int,
    val info: String,
    @ColumnInfo(name = "additional_info") val additionalInfo: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,

    @DrawableRes
    @ColumnInfo(name = "icon_res")
    val icon: Int
    //@ColumnInfo(name = "opening_info") val openingInfo: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FoodProvider

        if (fid != other.fid) return false
        if (name != other.name) return false
        if (locationId != other.locationId) return false
        if (type != other.type) return false
        if (info != other.info) return false
        if (additionalInfo != other.additionalInfo) return false
        if (isFavorite != other.isFavorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + locationId.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + info.hashCode()
        result = 31 * result + additionalInfo.hashCode()
        result = 31 * result + isFavorite.hashCode()
        return result
    }

}