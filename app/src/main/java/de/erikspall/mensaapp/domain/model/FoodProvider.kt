package de.erikspall.mensaapp.domain.model

import android.hardware.SensorAdditionalInfo
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.Locale

@IgnoreExtraProperties
data class FoodProvider(
    var name: String? = null,
    var location: String? = null,
    var category: String? = null,
    var type: String? = null,
    var address: String? = null,
    var photo: String? = null,
    var info: String? = null,
    var additionalInfo: String? = null
    //var description: Map<Locale, String> = mutableMapOf()
) {

    companion object {
        const val FIELD_LOCATION = "location"
        const val FIELD_CATEGORY = "category"
        const val FIELD_TYPE = "type"
        const val FIELD_ADDRESS = "address"
    }
}
