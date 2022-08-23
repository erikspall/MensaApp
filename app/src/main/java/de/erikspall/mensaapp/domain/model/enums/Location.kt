package de.erikspall.mensaapp.domain.model.enums

enum class Location(
    private val value: String
) {
    WUERZBURG("Würzburg"),
    BAMBERG("Bamberg"),
    SCHWEINFURT("Schweinfurt"),
    ASCHAFFENBURG("Aschaffenburg");

    override fun toString(): String {
        return value
    }
}