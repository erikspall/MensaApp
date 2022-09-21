package de.erikspall.mensaapp.domain.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.repositories.*
import de.erikspall.mensaapp.data.sources.local.database.AppDatabase
import de.erikspall.mensaapp.data.sources.local.database.daos.WeekdayDao
import de.erikspall.mensaapp.data.sources.remote.RemoteApiDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return AppDatabase.getDatabase(app)
    }

    @Provides
    @Singleton
    fun provideFoodProviderRepository(db: AppDatabase): FoodProviderRepository {
        return FoodProviderRepository(db.foodProviderDao())
    }

    @Provides
    @Singleton
    fun provideLocationRepository(db: AppDatabase): LocationRepository {
        return LocationRepository(db.locationDao())
    }

    @Provides
    @Singleton
    fun provideOpeningHoursRepository(db: AppDatabase): OpeningHoursRepository {
        return OpeningHoursRepository(db.openingHoursDao())
    }

    @Provides
    @Singleton
    fun provideWeekdayRepository(db: AppDatabase): WeekdayRepository {
        return WeekdayRepository(db.weekdayDao())
    }

    @Provides
    @Singleton
    fun provideFoodProviderTypeRepository(db: AppDatabase): FoodProviderTypeRepository {
        return FoodProviderTypeRepository(db.foodProviderTypeDao())
    }

    @Provides
    @Singleton
    fun provideApiDataSource(@IoDispatcher ioDispatcher: CoroutineDispatcher): RemoteApiDataSource {
        return RemoteApiDataSource(ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideAppRepository(
        foodProviderRepository: FoodProviderRepository,
        locationRepository: LocationRepository,
        openingHoursRepository: OpeningHoursRepository,
        weekdayRepository: WeekdayRepository,
        foodProviderTypeRepository: FoodProviderTypeRepository,
        apiDataSource: RemoteApiDataSource
    ): AppRepository {
        return AppRepository(
            foodProviderRepository,
            locationRepository,
            openingHoursRepository,
            weekdayRepository,
            foodProviderTypeRepository,
            apiDataSource
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext appContext: Context
    ) : SharedPreferences {
        return appContext.getSharedPreferences(appContext.getString(R.string.shared_pref_name), Context.MODE_PRIVATE)
    }
}