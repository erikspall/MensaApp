package de.erikspall.mensaapp.domain.model

import de.erikspall.mensaapp.domain.model.enums.Role
import de.erikspall.mensaapp.domain.model.interfaces.Food

class Meal(
    private val name: String,
    private val priceStudent: Int,
    private val priceEmployee: Int,
    private val priceGuest: Int,
    private val category: String = "Vegan"
) : Food {

    override fun getName(): String {
        return name
    }

    override fun getPrice(role: Role): Int {
        return when(role) {
            Role.STUDENT -> priceStudent
            Role.EMPLOYEE -> priceEmployee
            Role.GUEST -> priceGuest
        }
    }

    override fun getCategory(): String {
        return category
    }
}