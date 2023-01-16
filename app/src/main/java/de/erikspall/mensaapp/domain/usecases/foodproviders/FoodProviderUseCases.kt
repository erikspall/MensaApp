package de.erikspall.mensaapp.domain.usecases.foodproviders

data class FoodProviderUseCases(
    val fetchAll: FetchFoodProviders,
    val fetch: FetchFoodProvider,
    val fetchMenu: FetchMenu
)