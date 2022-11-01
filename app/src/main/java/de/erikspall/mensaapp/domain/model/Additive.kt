package de.erikspall.mensaapp.domain.model

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import de.erikspall.mensaapp.domain.enums.AdditiveType

//TODO: Make combination of name and type unique and ditch id
@Entity(tableName = "additives", primaryKeys = ["name", "type"])
data class Additive (


    var name: String = "",

    @DrawableRes
    @ColumnInfo(name = "icon_res")
    var icon: Int = 0,

    var type: AdditiveType = AdditiveType.ALLERGEN,

    val isNotLiked: Boolean = false,
) {


    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_TYPE = "type"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Additive

        if (name != other.name) return false
        if (icon != other.icon) return false
        if (type != other.type) return false
        if (isNotLiked != other.isNotLiked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon
        result = 31 * result + type.hashCode()
        result = 31 * result + isNotLiked.hashCode()
        return result
    }
}