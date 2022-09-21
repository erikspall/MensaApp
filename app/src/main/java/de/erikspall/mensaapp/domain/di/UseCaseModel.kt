package de.erikspall.mensaapp.domain.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.usecases.foodprovider.*
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModel {
    @Provides
    @Singleton
    fun provideFoodProvideUseCases(
        repository: AppRepository): FoodProviderUseCases {
        return FoodProviderUseCases(
            getFoodProviders = GetFoodProviders(repository),
            getOpeningHoursAsString = GetOpeningHoursAsString(),
            getInfoOfFoodProvider = GetInfoOfFoodProvider(repository),
            getMenus = GetMenus(repository)
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreferenceUseCases(
        @ApplicationContext appContext: Context,
        sharedPref: SharedPreferences
    ): SharedPreferenceUseCases {
        return SharedPreferenceUseCases(
            setValue = SetValue(appContext, sharedPref),
            getValue = GetValue(appContext, sharedPref),
            registerListener = RegisterListener(sharedPref),
            getValueRes = GetValueRes(appContext, sharedPref)
        )
    }
}