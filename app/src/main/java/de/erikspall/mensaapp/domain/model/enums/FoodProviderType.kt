package de.erikspall.mensaapp.domain.model.enums

enum class FoodProviderType(
    private val value: String
) {
    MENSA("Mensa"),
    MENSATERIA("Mensateria"),
    CAFETERIA("Cafeteria"),
    INTERIMENSA("Interimensa"),
    BURSE("Burse");

    override fun toString(): String {
        return value
    }
}