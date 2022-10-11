package de.erikspall.mensaapp.data.sources.remote.api.model

data class FoodProviderApiModel(
    val type: String,
    val name: String,
    val additionalInfo: String,
    val location: String,
    val openingHours: List<OpeningInfoApiModel>,
    val linkToFoodPlan: String,
    val id: Long,
    val info: String,
    val address: String,
    val description: String
    //val menus: List<MenuApiModel>
)