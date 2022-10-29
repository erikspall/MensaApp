package de.erikspall.mensaapp.data

import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.model.Additive
import java.time.LocalDateTime

data class FirestoreMeal (
    var name: String = "",
    var additives: List<Additive> = emptyList(),
    var prices: Map<Role, String> = emptyMap(),
    val date: LocalDateTime,
    val foodProviderId: Int
)