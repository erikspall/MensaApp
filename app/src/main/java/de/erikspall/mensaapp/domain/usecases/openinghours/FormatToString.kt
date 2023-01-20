package de.erikspall.mensaapp.domain.usecases.openinghours

import de.erikspall.mensaapp.MensaApplication
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

class FormatToString {
    operator fun invoke(
        openingHours: Map<DayOfWeek, List<Map<String, LocalTime>>>,
        currentDateTime: LocalDateTime,
        locale: Locale
    ): String {
        val res = MensaApplication.getRes()
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
                val currentDay = DayOfWeek.of((i % 7) + 1)
                val currentDate = currentDateTime.toLocalDate().plusDays(i - dayOffset)
                val openingHourList =
                    openingHours[currentDay] ?: return res.getString(R.string.closed)

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

                            val daysTo = ((timeTo / 60.0) / 24.0).roundToInt()

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
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                                            )
                                        )
                                    } else if (daysTo == 1) {
                                        res.getString(
                                            /* id = */ R.string.openingTimeTomorrow,
                                            /* ...formatArgs = */
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
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
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
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
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                                            )
                                        )
                                    } else if (daysTo == 1) {
                                        res.getString(
                                            /* id = */ R.string.endOfServiceTimeTomorrow,
                                            /* ...formatArgs = */
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
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
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
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
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                                            )
                                        )
                                    } else if (daysTo == 1) {
                                        res.getString(
                                            /* id = */ R.string.closingTimeTomorrow,
                                            /* ...formatArgs = */
                                            time.format(
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
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
                                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
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
                i++
            }
        } catch (e: Exception) {
            return res.getString(R.string.internal_error)
        }
        return res.getString(R.string.internal_error)
    }
}
