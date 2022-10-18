package de.erikspall.mensaapp.domain.usecases.openinghours

import de.erikspall.mensaapp.domain.model.OpeningHour
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*
import kotlin.math.ceil
import kotlin.math.round
import kotlin.math.roundToInt

class FormatToString {
    operator fun invoke(
        openingHours: Map<DayOfWeek, Map<String, LocalTime>>,
        currentDateTime: LocalDateTime,
        locale: Locale
    ): String {
        try {
            // Find the next 'event' of the day
            // Start at current day
            var i = currentDateTime.dayOfWeek.value
            val dayOffset = i.toLong()

            val fields = listOf(
                OpeningHour.FIELD_OPENS_AT,
                OpeningHour.FIELD_GET_FOOD_TILL,
                OpeningHour.FIELD_CLOSES_AT
            )
            // Only +6 because we already looked at that weekday
            while (i <= currentDateTime.dayOfWeek.value + 6) {
                val currentDay = DayOfWeek.of(i % 7)
                val currentDate = currentDateTime.toLocalDate().plusDays(i - dayOffset)
                val hourMap = openingHours[currentDay] ?: return "Geschlossen!"

                for (field in fields) {
                    val time = hourMap[field] ?: continue

                    val eventDateTime = LocalDateTime.of(
                        currentDate,
                        time
                    )
                    // Get duration to this event, if it doesnt exists ensure timeTo is -1 by
                    // substracting 1 minute of current time
                    val duration = Duration.between(
                        currentDateTime,
                        eventDateTime
                    )
                    val isBefore = currentDateTime < eventDateTime
                    if (isBefore) {
                        val timeTo = ceil(
                            (duration.toMillis() / 1000.0) / 60.0
                        ).roundToInt()

                        val daysTo = ((timeTo / 60.0) / 24.0).roundToInt()

                        // We found our event
                        return when (field) {
                            OpeningHour.FIELD_OPENS_AT -> {
                                if (timeTo < 60) {
                                    "Öffnet in $timeTo Minuten"
                                } else if (daysTo == 0) {
                                    "Öffnet um " + formatTimeToString(time, true, "")
                                } else if (daysTo == 1) {
                                    "Öffnet morgen um " + formatTimeToString(time, true, "")
                                } else {
                                    "Öffnet am ${
                                        currentDate.dayOfWeek.getDisplayName(
                                            TextStyle.SHORT_STANDALONE,
                                            Locale.getDefault()
                                        )
                                    } um " + formatTimeToString(time, true, "")
                                }
                            }
                            OpeningHour.FIELD_GET_FOOD_TILL -> {
                                if (timeTo < 60) {
                                    "Essensausgabe endet in $timeTo Minuten"
                                } else if (daysTo == 0) {
                                    "Essensausgabe endet um " + formatTimeToString(time, true, "")
                                } else if (daysTo == 1) {
                                    "Essensausgabe endet morgen um " + formatTimeToString(time, true, "")
                                } else {
                                    "Essensausgabe endet am ${
                                        currentDate.dayOfWeek.getDisplayName(
                                            TextStyle.SHORT_STANDALONE,
                                            Locale.getDefault()
                                        )
                                    } um " + formatTimeToString(time, true, "")
                                }
                            }
                            OpeningHour.FIELD_CLOSES_AT -> {
                                if (timeTo < 60) {
                                    "Schließt in $timeTo Minuten"
                                } else if (daysTo == 0) {
                                    "Schließt um " + formatTimeToString(time, true, "")
                                } else if (daysTo == 1) {
                                    "Schließt morgen um " + formatTimeToString(time, true, "")
                                } else {
                                    "Schließt am ${
                                        currentDate.dayOfWeek.getDisplayName(
                                            TextStyle.SHORT_STANDALONE,
                                            Locale.getDefault()
                                        )
                                    } um " + formatTimeToString(time, true, "")
                                }
                            }
                            else -> {
                                ""
                            }
                        }
                    }
                }
                i++
            }
        } catch (e: Exception) {
            return "Error parsing opening hours!"
        }
        return "Parsing opening hours failed!"
    }

    fun formatTimeToString(time: LocalTime, is24HoursFormat: Boolean, postFix: String): String {
        var amPM = ""
        var hours = time.hour
        if (!is24HoursFormat && hours > 12) {
            amPM = "pm"
        } else if (!is24HoursFormat) {
            amPM = "am"
        }
        if (amPM == "pm")
            hours -= 12
        return String.format(
            "%02d:%02d %s %s",
            hours,
            time.minute,
            amPM,
            postFix
        )
            .trimEnd()
    }
}
