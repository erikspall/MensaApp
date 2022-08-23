package de.erikspall.mensaapp.domain.model.interfaces

import de.erikspall.mensaapp.domain.model.MenuOfDay
import java.time.LocalDate

interface Menu {
    fun getDate(): LocalDate
    fun getMeals(): Set<Food>

    companion object {
        fun createMenuOfDay(
            date: LocalDate,
            food: Set<Food>
        ) : Menu {
            return MenuOfDay(
                date,
                food
            )
        }
    }
}