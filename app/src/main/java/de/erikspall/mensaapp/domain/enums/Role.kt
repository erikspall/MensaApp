package de.erikspall.mensaapp.domain.enums

import androidx.annotation.StringRes
import de.erikspall.mensaapp.R

enum class Role(@StringRes private val stringRes: Int): StringResEnum {
    STUDENT(R.string.role_student),
    GUEST(R.string.role_guest),
    EMPLOYEE(R.string.role_employee),
    INVALID(R.string.role_invalid);

    @StringRes
    override fun getValue(): Int {
        return stringRes
    }
}
