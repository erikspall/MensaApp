package de.erikspall.mensaapp.domain.model.interfaces

import de.erikspall.mensaapp.domain.model.OpeningHours
import java.time.DayOfWeek

interface OpeningInfo {
    fun getWeekday(): DayOfWeek
    fun isOpen(): Boolean
    fun getOpeningAt(): String
    fun getClosingAt(): String
    fun getGetAMealTill(): String

    companion object {
        fun createOpeningHours(
            dayOfWeek: DayOfWeek,
            isOpen: Boolean,
            openingAt: String,
            closingAt: String,
            getAMealTill: String
        ) : OpeningInfo {
            return OpeningHours(
                dayOfWeek,
                isOpen,
                openingAt,
                closingAt,
                getAMealTill
            )
        }
    }
}