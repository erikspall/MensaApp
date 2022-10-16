package de.erikspall.mensaapp.domain.enums

enum class Category(private val value: String) {
    CANTEEN("Canteen"),
    CAFETERIA("Cafeteria");

    fun getValue(): String {
        return value;
    }
}