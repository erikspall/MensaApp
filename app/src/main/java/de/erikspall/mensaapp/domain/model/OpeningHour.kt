package de.erikspall.mensaapp.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.time.DayOfWeek

@IgnoreExtraProperties
data class OpeningHour (
    var closesAt: String = "",
    var dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    var getFoodTill: String = "",
    var isOpen: Boolean = true,
    var opensAt: String = ""
)