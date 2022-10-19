package de.erikspall.mensaapp.data.sources.remote.firestore

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.erikspall.mensaapp.BuildConfig


class FirestoreInitializer : Initializer<FirebaseFirestore> {
    // The host '10.0.2.2' is a special IP address to let the
    // Android emulator connect to 'localhost'.
    private val FIRESTORE_EMULATOR_HOST = "192.168.0.18"
    private val FIRESTORE_EMULATOR_PORT = 8080

    override fun create(context: Context): FirebaseFirestore {
        val firestore = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        if (BuildConfig.DEBUG) {
            firestore.useEmulator(FIRESTORE_EMULATOR_HOST, FIRESTORE_EMULATOR_PORT)
            FirebaseFirestore.setLoggingEnabled(true)
        }
        firestore.firestoreSettings = settings
        return firestore
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}