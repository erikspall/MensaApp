package de.erikspall.mensaapp

import android.app.Application
import com.google.android.material.color.DynamicColors

class MensaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}