package de.erikspall.mensaapp.domain.model

import de.erikspall.mensaapp.domain.enums.AdditiveType

data class Additive (
    var name: String = "",
    var type: AdditiveType = AdditiveType.ALLERGEN
) {
    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_TYPE = "type"
    }
}