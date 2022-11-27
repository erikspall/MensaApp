package de.erikspall.mensaapp.data.handler

import com.google.firebase.firestore.Source
import de.erikspall.mensaapp.data.requests.FoodProviderRequest
import de.erikspall.mensaapp.domain.interfaces.data.Handler
import de.erikspall.mensaapp.domain.interfaces.data.Request
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import java.time.Duration
import java.time.LocalDateTime

class SaveTimeHandler(
    override val next: Handler? = null,
    private val sharedPreferenceUseCases: SharedPreferenceUseCases
) : Handler {
    override fun handle(request: Request) {
        if (request.source != Source.CACHE) {

            sharedPreferenceUseCases.setLocalDateTime(
                request.requestId,
                LocalDateTime.now()
            )

        }
    }
}