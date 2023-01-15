package de.erikspall.mensaapp.domain.const

import de.erikspall.mensaapp.domain.model.FoodProvider

object SharedPrefKey {
    const val ROLE = "role"
    const val LOCATION = "location"
    const val WARNING = "warning"

    fun constructFoodProviderKey(foodProvider: FoodProvider): String {
        return "${foodProvider.type}_${foodProvider.name}_${foodProvider.location}"
    }
}