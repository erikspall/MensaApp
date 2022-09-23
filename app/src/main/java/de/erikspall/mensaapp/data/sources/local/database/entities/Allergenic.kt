package de.erikspall.mensaapp.data.sources.local.database.entities

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "allergenic")
data class Allergenic(
        @PrimaryKey
        private val name: String,

        @DrawableRes
        @ColumnInfo(name = "icon_res")
        val icon: Int,

        private val userDoesNotLike: Boolean = false
): MealComponent {
        override fun getName(): String {
                return name
        }


        override fun getUserDoesNotLike(): Boolean {
                return userDoesNotLike
        }

}
