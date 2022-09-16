package de.erikspall.mensaapp.data.sources.remote.api.model

import de.erikspall.mensaapp.domain.model.enums.FoodProviderType
import de.erikspall.mensaapp.domain.model.enums.Location
import de.erikspall.mensaapp.domain.model.interfaces.Menu
import de.erikspall.mensaapp.domain.model.interfaces.OpeningInfo

data class FoodProviderApiModel(
    val id: Int,
    val name: String,
    val openingHours: List<OpeningInfoApiModel>,
    val info: String,
    val additionalInfo: String,
    val linkToFoodPlan: String,
    val menus: List<MenuApiModel>
)