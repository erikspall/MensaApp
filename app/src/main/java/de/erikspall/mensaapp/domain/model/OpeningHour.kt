package de.erikspall.mensaapp.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.time.DayOfWeek

data class OpeningHour (
    var closesAt: String,
    var dayOfWeek: DayOfWeek,
    var getFoodTill: String,
    var isOpen: Boolean,
    var opensAt: String
)