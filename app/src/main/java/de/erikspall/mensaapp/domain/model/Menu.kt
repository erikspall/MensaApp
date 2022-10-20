package de.erikspall.mensaapp.domain.model

import java.time.LocalDate

data class Menu(
    val date: LocalDate,
    val meals: List<Meal>
)
