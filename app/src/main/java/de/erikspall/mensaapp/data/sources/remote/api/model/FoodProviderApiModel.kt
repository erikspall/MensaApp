package de.erikspall.mensaapp.data.sources.remote.api.model

data class FoodProviderApiModel(
    val id: Long,
    val name: String,
    val location: LocationApiModel,
    val openingHours: List<OpeningInfoApiModel>,
    val info: String,
    val additionalInfo: String,
    val linkToFoodPlan: String,
    val menus: List<MenuApiModel>
)