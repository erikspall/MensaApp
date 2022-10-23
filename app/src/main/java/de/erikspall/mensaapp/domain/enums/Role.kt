package de.erikspall.mensaapp.domain.enums

import androidx.annotation.StringRes
import de.erikspall.mensaapp.R

enum class Role(
    @StringRes private val stringRes: Int,
    private val rawValue: String
    ): StringResEnum {
    STUDENT(R.string.role_student, "Student"),
    GUEST(R.string.role_guest, "Gast"),
    EMPLOYEE(R.string.role_employee, "Bediensteter"),
    INVALID(R.string.role_invalid, "Ungültig");

    @StringRes
    override fun getValue(): Int {
        return stringRes
    }

    override fun getRawValue(): String {
        return rawValue
    }
}
