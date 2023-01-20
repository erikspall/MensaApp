package de.erikspall.mensaapp

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MensaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        res = resources
    }

    companion object {
        private lateinit var res: Resources

        fun getRes(): Resources {
            return res
        }
    }
}