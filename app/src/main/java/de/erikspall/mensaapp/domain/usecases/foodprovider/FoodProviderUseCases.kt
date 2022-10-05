package de.erikspall.mensaapp.domain.usecases.foodprovider

data class FoodProviderUseCases (
    val getFoodProviders: GetFoodProviders,
    val getOpeningHoursAsString: GetOpeningHoursAsString,
    val getInfoOfFoodProvider: GetInfoOfFoodProvider,
    val getMenus: GetMenus,
    val fetchLatest: FetchLatest
)