package de.erikspall.mensaapp.domain.interfaces.data

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.local.QueryResult
import de.erikspall.mensaapp.data.sources.remote.firestore.FirestoreDataSource
import java.time.Duration
import java.time.LocalTime

interface Request {
    val requestId: String
    val expireDuration: Duration
    var source: Source

    suspend fun execute(firestoreDataSource: FirestoreDataSource): Result<QuerySnapshot>
}