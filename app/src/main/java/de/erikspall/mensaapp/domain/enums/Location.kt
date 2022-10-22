package de.erikspall.mensaapp.domain.enums

import androidx.annotation.StringRes
import de.erikspall.mensaapp.R

enum class Location(
    @StringRes private val stringRes: Int,
    private val rawRes: String
    ): StringResEnum {
    WUERZBURG(R.string.location_wuerzburg, "Würzburg"),
    ASCHAFFENBURG(R.string.location_aschaffenburg, "Aschaffenburg"),
    BAMBERG(R.string.location_bamberg, "Bamberg"),
    SCHWEINFURT(R.string.location_schweinfurt, "Schweinfurt"),
    ANY(R.string.location_invalid, "Ungültig");

    @StringRes
    override fun getValue(): Int {
        return stringRes
    }

    override fun getRawValue(): String {
        return rawRes
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