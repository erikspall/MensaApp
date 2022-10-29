package de.erikspall.mensaapp.domain.usecases.openinghours

import de.erikspall.mensaapp.domain.model.OpeningHour
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

class OpeningHourUseCasesTest {
    // Subjects under test
    private lateinit var openingHourUseCases: OpeningHourUseCases
    private lateinit var standardOpeningHours: Map<DayOfWeek, List<Map<String, LocalTime>>>
    private lateinit var multipleOpeningHoursOnSameDay: Map<DayOfWeek, List<Map<String, LocalTime>>>

    @Before
    fun setupOpeningHourUseCases() {
        openingHourUseCases = OpeningHourUseCases(FormatToString())
    }

    @Before
    fun setupOpeningHours() {
        standardOpeningHours = mapOf(
            DayOfWeek.MONDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_GET_FOOD_TILL to LocalTime.of(16, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(16, 30)
                )
            ),
            DayOfWeek.TUESDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_GET_FOOD_TILL to LocalTime.of(16, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(16, 30)
                )
            ),
            DayOfWeek.WEDNESDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_GET_FOOD_TILL to LocalTime.of(16, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(16, 30)
                )
            ),
            DayOfWeek.THURSDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_GET_FOOD_TILL to LocalTime.of(16, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(16, 30)
                )
            ),
            DayOfWeek.FRIDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_GET_FOOD_TILL to LocalTime.of(13, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(13, 30)
                )
            )
        )

        multipleOpeningHoursOnSameDay = mapOf(
            DayOfWeek.MONDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(8, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(10, 0)
                ),
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(13, 0)
                )
            ),
            DayOfWeek.TUESDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(8, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(10, 0)
                ),
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(13, 0)
                )
            ),
            DayOfWeek.WEDNESDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(8, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(10, 0)
                ),
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(13, 0)
                )
            ),
            DayOfWeek.THURSDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(8, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(10, 0)
                ),
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(13, 0)
                )
            ),
            DayOfWeek.FRIDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(8, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(10, 0)
                ),
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(11, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(13, 0)
                )
            )
        )
    }

    @Test
    fun formatToString_noData_returnsClosed() {
        // Given an empty map of opening hours
        val emptyOpeningHours = emptyMap<DayOfWeek, List<Map<String, LocalTime>>>()

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            emptyOpeningHours,
            LocalDateTime.now(),
            Locale.getDefault() // Doesn't matter for now
        )

        // Then formatted string is "Geschlossen!"
        assertEquals("Geschlossen!", string)
    }

    @Test
    fun formatToString_oneHourBeforeOpening_returnsOpeningAt() {
        // Given a LocalDateTime 1h before opening
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(10, 0)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault() // Doesn't matter for now
        )

        // Then formatted string is "Öffnet um 11:00 Uhr"
        assertEquals("Öffnet um 11:00 Uhr", string)
    }

    @Test
    fun formatToString_exactlyThirtyMinutesBeforeOpening_returnsOpeningInThirtyMinutes() {
        // Given a LocalDateTime exactly 30 min before opening
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(10, 30)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string is "Öffnet in 30 Minuten"
        assertEquals("Öffnet in 30 Minuten", string)
    }

    @Test
    fun formatToString_oneMinuteBeforeOpening_returnsOpeningInOneMinute() {
        // Given a LocalDateTime exactly 1 min before opening
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(10, 59)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string is "Öffnet in 1 Minute"
        assertEquals("Öffnet in 1 Minute", string)
    }

    @Test
    fun formatToString_onOpening_returnsGetAMealTill() {
        // Given a LocalDateTime exactly on opening time
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(11, 0)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string is "Öffnet in 1 Minute"
        assertEquals("Geöffnet! Service bis 16:00 Uhr", string)
    }

    @Test
    fun formatToString_oneHourBeforeServiceEnd_returnsGetAMealTill() {
        // Given a LocalDateTime exactly on opening time
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(15, 0)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string is "Öffnet in 1 Minute"
        assertEquals("Geöffnet! Service bis 16:00 Uhr", string)
    }

    @Test
    fun formatToString_belowOneHourBeforeServiceEnds_returnsServiceEndsIn() {
        // Given a LocalDateTime exactly on opening time
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(15, 30)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string is
        assertEquals("Service endet in 30 Minuten", string)
    }

    @Test
    fun formatToString_exactlyOnServiceEnd_returnsClosing() {
        // Given a LocalDateTime exactly on opening time
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(16, 0)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string contains
        assertTrue(string.contains("Schließt"))
    }

    @Test
    fun formatToString_afterOnServiceEnd_returnsClosing() {
        // Given a LocalDateTime exactly on opening time
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(16, 15)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string contains
        assertTrue(string.contains("Schließt"))
    }

    @Test
    fun formatToString_onClosing_returnsOpeningAtTomorrow() {
        // Given a LocalDateTime exactly on opening time
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(16, 30)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string contains
        assertEquals("Geschlossen! Öffnet wieder morgen um 11:00 Uhr", string)
    }

    @Test
    fun formatToString_afterClosingButBeforeTheDayOfOpening_returnsOpeningAtTomorrow() {
        // Given a LocalDateTime exactly on opening time
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25), // Tuesday
            LocalTime.of(23, 30)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string contains
        assertEquals("Geschlossen! Öffnet wieder morgen um 11:00 Uhr", string)
    }

    @Test
    fun formatToString_afterClosingLastDayOfWeek_returnsOpeningAtFirstDayOfNextWeek() {
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 28),
            LocalTime.of(18, 0)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            standardOpeningHours,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string contains
        assertEquals("Geschlossen! Öffnet am Mo um 11:00 Uhr", string)
    }

    @Test
    fun formatToString_afterClosingBeforeSecondOpening_returnsOpeningAt() {
        val localDateTime = LocalDateTime.of(
            LocalDate.of(2022, 10, 25),
            LocalTime.of(10, 0)
        )

        // When formatting to string
        val string = openingHourUseCases.formatToString(
            multipleOpeningHoursOnSameDay,
            localDateTime,
            Locale.getDefault()
        )

        // Then formatted string contains
        assertEquals("Geschlossen! Öffnet wieder um 11:00 Uhr", string)
    }
}