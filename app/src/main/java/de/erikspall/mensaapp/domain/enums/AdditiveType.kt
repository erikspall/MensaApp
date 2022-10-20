package de.erikspall.mensaapp.domain.enums

enum class AdditiveType(
    private val typeName: String
) {
    ALLERGEN("allergen"),
    INGREDIENT("ingredient");

    fun getValue(): String {
        return typeName
    }

    companion object {
        fun from(str: String): AdditiveType? {
            return when (str) {
                "allergen" -> ALLERGEN
                "ingredient" -> INGREDIENT
                else -> null
            }
        }
    }
}