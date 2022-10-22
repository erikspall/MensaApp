package de.erikspall.mensaapp.domain.enums

import androidx.annotation.StringRes
import de.erikspall.mensaapp.R

enum class Location(@StringRes private val stringRes: Int): StringResEnum {
    WUERZBURG(R.string.location_wuerzburg),
    ASCHAFFENBURG(R.string.location_aschaffenburg),
    BAMBERG(R.string.location_bamberg),
    SCHWEINFURT(R.string.location_schweinfurt),
    ANY(R.string.location_invalid);

    @StringRes
    override fun getValue(): Int {
        return stringRes
    }

    companion object {
        fun from(str: String): Location {
            return when (str) {
                "Aschaffenburg" -> ASCHAFFENBURG
                "Bamberg" -> BAMBERG
                "Schweinfurt" -> SCHWEINFURT
                "Würzburg" -> WUERZBURG
                else -> {
                    ANY
                }
            }
        }
    }
}