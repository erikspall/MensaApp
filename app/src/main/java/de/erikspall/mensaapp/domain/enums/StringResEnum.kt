package de.erikspall.mensaapp.domain.enums

import androidx.annotation.StringRes
import de.erikspall.mensaapp.R

interface StringResEnum {
    @StringRes
    fun getValue(): Int

    companion object {
        fun roleFrom(@StringRes stringRes: Int): Role {
            return when (stringRes) {
                R.string.role_student -> Role.STUDENT
                R.string.role_guest -> Role.GUEST
                R.string.role_employee -> Role.EMPLOYEE
                else -> Role.INVALID
            }
        }

        fun locationFrom(@StringRes stringRes: Int): Location {
            return when (stringRes) {
                R.string.location_aschaffenburg -> Location.ASCHAFFENBURG
                R.string.location_bamberg -> Location.BAMBERG
                R.string.location_schweinfurt -> Location.SCHWEINFURT
                R.string.location_wuerzburg -> Location.WUERZBURG
                else -> {
                    Location.ANY
                }
            }
        }
    }
}