package de.erikspall.mensaapp.domain.usecases.foodprovider.order

sealed class FoodProviderOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): FoodProviderOrder(orderType)
    class IsFavorite(orderType: OrderType): FoodProviderOrder(orderType)
}