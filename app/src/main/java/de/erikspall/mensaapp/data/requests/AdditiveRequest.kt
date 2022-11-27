package de.erikspall.mensaapp.data.requests

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import de.erikspall.mensaapp.data.sources.remote.firestore.FirestoreDataSource
import de.erikspall.mensaapp.domain.interfaces.data.Request
import java.time.Duration

class AdditiveRequest(
    override val requestId: String = "additives",
    override val expireDuration: Duration = Duration.ofDays(30),
    override var source: Source = Source.CACHE
) : Request {
    override suspend fun execute(firestoreDataSource: FirestoreDataSource): Result<QuerySnapshot> {
        return firestoreDataSource.fetchAdditives(
            source = source
        )
    }
}