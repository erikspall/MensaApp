package de.erikspall.mensaapp

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import de.erikspall.mensaapp.data.sources.local.database.AppDatabase

@HiltAndroidApp
class MensaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}