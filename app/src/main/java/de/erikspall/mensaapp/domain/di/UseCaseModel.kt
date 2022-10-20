package de.erikspall.mensaapp.domain.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.domain.usecases.foodproviders.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.foodproviders.GetFoodProvider
import de.erikspall.mensaapp.domain.usecases.foodproviders.GetFoodProviders
import de.erikspall.mensaapp.domain.usecases.foodproviders.GetMenusOfFoodProviderFromDate
import de.erikspall.mensaapp.domain.usecases.mealcomponents.*
import de.erikspall.mensaapp.domain.usecases.openinghours.FormatToString
import de.erikspall.mensaapp.domain.usecases.openinghours.OpeningHourUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModel {

    @Provides
    @Singleton
    fun provideFoodProviderUseCases(
        appRepository: AppRepository
    ): FoodProviderUseCases {
        return FoodProviderUseCases(
            getAll = GetFoodProviders(appRepository),
            get = GetFoodProvider(appRepository),
            getMenusOfFoodProviderFromDate = GetMenusOfFoodProviderFromDate(appRepository)
        )
    }

    @Provides
    @Singleton
    fun provideOpeningHourUseCases(

    ): OpeningHourUseCases {
        return OpeningHourUseCases(
            formatToString = FormatToString()
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
            getValueRes = GetValueRes(appContext, sharedPref),
            setBoolean = SetBoolean(appContext, sharedPref),
            getBoolean = GetBoolean(appContext, sharedPref)
        )
    }

    @Provides
    @Singleton
    fun provideMealComponentUseCases(
        appRepository: AppRepository
    ): MealComponentUseCases {
        return MealComponentUseCases(
            setIngredientLikeStatus = SetIngredientLikeStatus(appRepository),
            setAllergenLikeStatus = SetAllergenLikeStatus(appRepository),
            getAllergens = GetAllergens(appRepository),
            getIngredients = GetIngredients(appRepository),
            fetchLatest = FetchLatest(appRepository)
        )
    }
}