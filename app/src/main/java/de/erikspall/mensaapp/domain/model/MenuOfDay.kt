package de.erikspall.mensaapp.domain.model

import de.erikspall.mensaapp.domain.model.interfaces.Food
import de.erikspall.mensaapp.domain.model.interfaces.Menu
import java.time.LocalDate

class MenuOfDay(
    private val date: LocalDate,
    private val food: Set<Food>
) : Menu {
    override fun getDate(): LocalDate {
        return date
    }

    override fun getMeals(): Set<Food> {
        return food
    }
}