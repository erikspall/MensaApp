package de.erikspall.mensaapp.domain.model.interfaces

import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.domain.model.enums.Role

interface Food {
    fun getName(): String
    fun getPrice(role: Role): Int
   // fun getIngredients(): String

    companion object {
        fun createMeal(
            name: String,
            priceStudent: Int,
            priceEmployee: Int,
            priceGuest: Int
        ): Food {
            return Meal(
                name,
                priceStudent,
                priceEmployee,
                priceGuest
            )
        }
    }
}