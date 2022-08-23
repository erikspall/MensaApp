package de.erikspall.mensaapp.domain.model

import de.erikspall.mensaapp.domain.model.interfaces.OpeningInfo
import java.time.DayOfWeek

class OpeningHours(
    private val dayOfWeek: DayOfWeek,
    private val isOpen: Boolean,
    private val openingAt: String,
    private val closingAt: String,
    private val getAMealTill: String
) : OpeningInfo {
    override fun getWeekday(): DayOfWeek {
        return dayOfWeek
    }

    override fun isOpen(): Boolean {
        return isOpen
    }

    override fun getOpeningAt(): String {
        return openingAt
    }

    override fun getClosingAt(): String {
        return closingAt
    }

    override fun getGetAMealTill(): String {
        return getAMealTill
    }
}