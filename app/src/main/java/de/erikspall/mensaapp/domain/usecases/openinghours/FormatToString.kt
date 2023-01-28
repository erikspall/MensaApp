package de.erikspall.mensaapp.domain.usecases.openinghours

import android.content.res.Resources

import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.model.OpeningHour
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt

class FormatToString(
    val res: Resources
) {
    operator fun invoke(
        openingHours: Map<DayOfWeek, List<Map<String, LocalTime>>>,
        currentDateTime: LocalDateTime
    ): String {
       // val res = MensaApplication.getRes()
        try {
            if (openingHours.isEmpty()) return res.getString(R.string.closed)
            // Find the next 'event' of the day
            // Start at current dayOfWeek
            val dayOffset = currentDateTime.dayOfWeek.value.toLong()


            val fields = listOf(
                OpeningHour.FIELD_OPENS_AT,
                OpeningHour.FIELD_GET_FOOD_TILL,
                OpeningHour.FIELD_CLOSES_AT
            )
            // While the currently looked at dayOfWeek is smaller or equal than that
            // day plus 6 days (we don't look at the next 7, because we looked at that weekday
            // already
            for (i in currentDateTime.dayOfWeek.value..currentDateTime.dayOfWeek.value + 6) {
                // DayOfWeek enum starts at 1
                val modulo = if (i % 8 == 0) 1 else i % 8
                // Determine the dayOfWeek, that we are looking at in this iteration
                val currentDay = DayOfWeek.of(modulo)
                // Determine the date, that we are looking at in this iteration
                val currentDate = currentDateTime.toLocalDate().plusDays(i - dayOffset)

                val openingHourList =
                    openingHours[currentDay] ?: continue

                for (field in fields) {
                    for (hourMap in openingHourList) {
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

                            val daysTo = eventDateTime.dayOfYear - currentDateTime.dayOfYear

                            // We found our event
                            return when (field) {
                                OpeningHour.FIELD_OPENS_AT -> {
                                    if (timeTo < 60) {
                                        res.getQuantityString(
                                            /* id = */ R.plurals.minutesTillOpening,
                                            /* quantity = */ timeTo,
                                            /* ...formatArgs = */ timeTo
                                        )
                                    } else if (daysTo == 0) {
                                        res.getString(
                                            /* id = */ R.string.openingTimeToday,
                                            /* ...formatArgs = */
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
                                            )
                                        )
                                    } else if (daysTo == 1) {
                                        res.getString(
                                            /* id = */ R.string.openingTimeTomorrow,
                                            /* ...formatArgs = */
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
                                            )
                                        )
                                    } else {
                                        res.getString(
                                            /* id = */ R.string.openingTimeBeyond,
                                            /* ...formatArgs = */
                                            currentDate.dayOfWeek.getDisplayName(
                                                TextStyle.SHORT_STANDALONE,
                                                Locale.getDefault()
                                            ),
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
                                            )
                                        )
                                    }
                                }
                                OpeningHour.FIELD_GET_FOOD_TILL -> {
                                    if (timeTo < 60) {
                                        res.getQuantityString(
                                            /* id = */ R.plurals.minutesTillEndOfService,
                                            /* quantity = */ timeTo,
                                            /* ...formatArgs = */ timeTo
                                        )
                                    } else if (daysTo == 0) {
                                        res.getString(
                                            /* id = */ R.string.endOfServiceTimeToday,
                                            /* ...formatArgs = */
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
                                            )
                                        )
                                    } else if (daysTo == 1) {
                                        res.getString(
                                            /* id = */ R.string.endOfServiceTimeTomorrow,
                                            /* ...formatArgs = */
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
                                            )
                                        )
                                    } else {
                                        res.getString(
                                            /* id = */ R.string.endOfServiceTimeBeyond,
                                            /* ...formatArgs = */
                                            currentDate.dayOfWeek.getDisplayName(
                                                TextStyle.SHORT_STANDALONE,
                                                Locale.getDefault()
                                            ),
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
                                            )
                                        )
                                    }
                                }
                                OpeningHour.FIELD_CLOSES_AT -> {
                                    if (timeTo < 60) {
                                        res.getQuantityString(
                                            /* id = */ R.plurals.minutesTillClosing,
                                            /* quantity = */ timeTo,
                                            /* ...formatArgs = */ timeTo
                                        )
                                    } else if (daysTo == 0) {
                                        res.getString(
                                            /* id = */ R.string.closingTimeToday,
                                            /* ...formatArgs = */
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
                                            )
                                        )
                                    } else if (daysTo == 1) {
                                        res.getString(
                                            /* id = */ R.string.closingTimeTomorrow,
                                            /* ...formatArgs = */
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
                                            )
                                        )
                                    } else {
                                        res.getString(
                                            /* id = */ R.string.closingTimeBeyond,
                                            /* ...formatArgs = */
                                            currentDate.dayOfWeek.getDisplayName(
                                                TextStyle.SHORT_STANDALONE,
                                                Locale.getDefault()
                                            ),
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
                                            )
                                        )
                                    }
                                }
                                else -> {
                                    res.getString(R.string.internal_error)
                                }
                            }
                        }
                    }

                }

            }
            // We should never reach this, but just in case
            return res.getString(R.string.closed)
        } catch (e: Exception) {
            return res.getString(R.string.internal_error)
        }
    }
}
