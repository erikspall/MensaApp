package de.erikspall.mensaapp.domain.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModel {


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
}