package de.erikspall.mensaapp.data.handler

import com.google.firebase.firestore.Source
import de.erikspall.mensaapp.domain.interfaces.data.Handler
import de.erikspall.mensaapp.domain.interfaces.data.Request
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import java.time.Duration
import java.time.LocalDateTime

/**
 * Decides and sets the source to use
 */
class SourceHandler(
    override val next: Handler? = null,
    private val sharedPreferenceUseCases: SharedPreferenceUseCases
) : Handler {
    override fun handle(request: Request) {
        // Get current DateTime
        val currentDateTime = LocalDateTime.now()

        // Get last update
        val lastUpdate = sharedPreferenceUseCases.getLocalDateTime(
            request.requestId,
            currentDateTime.minusDays(365)
        )

        val duration = Duration.between(lastUpdate, currentDateTime)

        request.source = if (duration > request.expireDuration) {
             Source.DEFAULT
        } else {
            Source.CACHE
        }
        next?.handle(request)
    }
}