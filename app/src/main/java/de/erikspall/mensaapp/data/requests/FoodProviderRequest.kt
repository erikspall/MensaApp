package de.erikspall.mensaapp.data.requests

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.local.QueryResult
import de.erikspall.mensaapp.data.sources.remote.firestore.FirestoreDataSource
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.interfaces.data.Request
import java.time.Duration

class FoodProviderRequest(
    override val requestId: String,
    override val expireDuration: Duration = Duration.ofDays(7),
    override var source: Source = Source.CACHE,
    val parameters: FoodProviderRequestParameters
): Request {
    override suspend fun execute(firestoreDataSource: FirestoreDataSource): Result<QuerySnapshot> {
        return if (parameters.foodProviderId == -1) {
            firestoreDataSource.fetchFoodProviders(
                source = source,
                location = parameters.location,
                category = parameters.category
            )
        } else {
            firestoreDataSource.fetchFoodProvider(
                foodProviderId = parameters.foodProviderId,
                source = source
            )
        }
    }
}

data class FoodProviderRequestParameters(
    val foodProviderId: Int = -1,
    val category: Category = Category.ANY,
    val location: Location = Location.ANY
)