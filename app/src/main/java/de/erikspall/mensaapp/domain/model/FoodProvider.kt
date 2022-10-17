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
        const val FIELD_NAME = "name"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FoodProvider

        if (name != other.name) return false
        if (location != other.location) return false
        if (category != other.category) return false
        if (type != other.type) return false
        if (address != other.address) return false
        if (photo != other.photo) return false
        if (info != other.info) return false
        if (additionalInfo != other.additionalInfo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (photo?.hashCode() ?: 0)
        result = 31 * result + (info?.hashCode() ?: 0)
        result = 31 * result + (additionalInfo?.hashCode() ?: 0)
        return result
    }
}
