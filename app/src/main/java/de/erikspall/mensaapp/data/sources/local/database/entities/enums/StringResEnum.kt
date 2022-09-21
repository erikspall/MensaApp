package de.erikspall.mensaapp.data.sources.local.database.entities.enums

import androidx.annotation.StringRes
import de.erikspall.mensaapp.R

interface StringResEnum {
    @StringRes
    fun getValue(): Int

    companion object {
        fun roleFrom(@StringRes stringRes: Int): StringResEnum {
            return when (stringRes) {
                R.string.role_student -> Role.STUDENT
                R.string.role_guest -> Role.GUEST
                R.string.role_employee -> Role.EMPLOYEE
                else -> Role.INVALID
            }
        }
    }
}