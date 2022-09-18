package de.erikspall.mensaapp.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.usecases.foodprovider.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.foodprovider.GetFoodProvidersWithoutMenu
import de.erikspall.mensaapp.domain.usecases.foodprovider.GetInfoOfFoodProvider
import de.erikspall.mensaapp.domain.usecases.foodprovider.GetOpeningHoursAsString
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModel {
    @Provides
    @Singleton
    fun provideFoodProvideUseCases(repository: AppRepository): FoodProviderUseCases {
        return FoodProviderUseCases(
            getFoodProvidersWithoutMenu = GetFoodProvidersWithoutMenu(repository),
            getOpeningHoursAsString = GetOpeningHoursAsString(),
            getInfoOfFoodProvider = GetInfoOfFoodProvider(repository)
        )
    }
}