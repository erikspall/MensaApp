package de.erikspall.mensaapp.data.sources.local.database.entities

import java.time.LocalDate

// Not a database entity
data class Menu (
    val date: LocalDate,
    val meals: List<Meal>
)