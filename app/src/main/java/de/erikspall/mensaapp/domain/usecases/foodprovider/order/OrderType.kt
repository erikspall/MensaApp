package de.erikspall.mensaapp.domain.usecases.foodprovider.order

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}