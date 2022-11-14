package de.erikspall.mensaapp.domain.model

import de.erikspall.mensaapp.domain.utils.Extensions.equalsIgnoreOrder
import java.time.LocalDate

data class Menu(
    val date: LocalDate,
    val meals: List<Meal>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Menu

        if (date != other.date) return false
        if (!meals.equalsIgnoreOrder(other.meals)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + meals.hashCode()
        return result
    }
}
