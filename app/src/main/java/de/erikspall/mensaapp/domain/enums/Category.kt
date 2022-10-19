package de.erikspall.mensaapp.domain.enums

enum class Category(private val value: String) {
    CANTEEN("Canteen"),
    CAFETERIA("Cafeteria"),
    UNKNOWN("Unknown");

    fun getValue(): String {
        return value;
    }

    companion object {
        fun from(str: String): Category {
            return when (str) {
                "Canteen" -> CANTEEN
                "Cafeteria" -> CAFETERIA
                else -> UNKNOWN
            }
        }
    }

}