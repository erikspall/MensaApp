package de.erikspall.mensaapp.domain.model

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import de.erikspall.mensaapp.domain.enums.AdditiveType

//TODO: Make combination of name and type unique and ditch id
@Entity(tableName = "additives")
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
}