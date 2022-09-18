package de.erikspall.mensaapp.domain.usecases.foodprovider

import android.icu.text.DateFormat.getDateInstance
import de.erikspall.mensaapp.data.sources.local.database.entities.OpeningHours
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

// TODO: Make it more dynamic
class GetOpeningHoursAsString {
    operator fun invoke(
        openingHours: List<OpeningHours>
    ): String {
        val temp = openingHours.sortedBy { it.weekdayId }.filter { it.opensAt.isNotBlank() }
        val first = DayOfWeek.of(temp.first().weekdayId.toInt()).getDisplayName(TextStyle.SHORT, Locale.getDefault()).removeSuffix(".")
        val last = DayOfWeek.of(temp.last().weekdayId.toInt()).getDisplayName(TextStyle.SHORT, Locale.getDefault()).removeSuffix(".")
        return "$first - $last: ${temp.first().opensAt} - ${temp.last().closesAt} Uhr"
    }


}