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
import de.erikspall.mensaapp.data.sources.remote.api.RemoteApiDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
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
    fun provideAllergenicRepository(db: AppDatabase): AllergenicRepository {
        return AllergenicRepository(db.allergenicDao())
    }

    @Provides
    @Singleton
    fun provideIngredientRepository(db: AppDatabase): IngredientRepository {
        return IngredientRepository(db.ingredientsDao())
    }



    @Provides
    @Singleton
    fun provideApiDataSource(@IoDispatcher ioDispatcher: CoroutineDispatcher): RemoteApiDataSource {
        return RemoteApiDataSource(ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideAppRepository(
        allergenicRepository: AllergenicRepository,
        ingredientRepository: IngredientRepository,
        apiDataSource: RemoteApiDataSource,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): AppRepository {
        return AppRepository(
            allergenicRepository,
            ingredientRepository,
            apiDataSource,
            CoroutineScope(defaultDispatcher)
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