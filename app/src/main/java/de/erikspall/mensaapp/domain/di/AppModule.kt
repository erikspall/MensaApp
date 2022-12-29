package de.erikspall.mensaapp.domain.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.erikspall.mensaapp.data.handler.SaveTimeHandler
import de.erikspall.mensaapp.data.handler.SourceHandler
import de.erikspall.mensaapp.data.repositories.*
import de.erikspall.mensaapp.domain.interfaces.data.AdditiveRepository
import de.erikspall.mensaapp.domain.interfaces.data.AppRepository
import de.erikspall.mensaapp.domain.interfaces.data.FirestoreRepository
import de.erikspall.mensaapp.data.sources.local.database.AppDatabase
import de.erikspall.mensaapp.data.sources.remote.firestore.FirestoreDataSource
import de.erikspall.mensaapp.domain.const.SharedPref
import de.erikspall.mensaapp.domain.usecases.openinghours.OpeningHourUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
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
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firestoreDataSource: FirestoreDataSource,
        openingHourUseCases: OpeningHourUseCases,
        sharedPreferenceUseCases: SharedPreferenceUseCases
    ): FirestoreRepository {
        return FirestoreRepositoryImpl(
            firestoreDataSource,
            openingHourUseCases,
            SourceHandler(sharedPreferenceUseCases = sharedPreferenceUseCases),
            SaveTimeHandler(sharedPreferenceUseCases = sharedPreferenceUseCases)
        )
    }

    @Provides
    @Singleton
    fun provideFirestoreDataSource(
        firebaseFirestore: FirebaseFirestore,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): FirestoreDataSource {
        return FirestoreDataSource(
            firestoreInstance = firebaseFirestore,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideAdditiveRepository(
        db: AppDatabase
    ): AdditiveRepository {
        return AdditiveRepositoryImpl(db.additiveDao())
    }


    @Provides
    @Singleton
    fun provideAppRepository(
        additiveRepository: AdditiveRepository,
        firestoreRepository: FirestoreRepository
    ): AppRepository {
        return AppRepositoryImpl(
            additiveRepository,
            firestoreRepository
        )
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext appContext: Context
    ): SharedPreferences {
        return appContext.getSharedPreferences(
            SharedPref.NAME,
            Context.MODE_PRIVATE
        )
    }
}