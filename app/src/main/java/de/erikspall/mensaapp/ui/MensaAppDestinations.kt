package de.erikspall.mensaapp.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.FoodBank
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import de.erikspall.mensaapp.R

interface MensaAppDestination {
    val icon: ImageVector
    val route: String
    @get:StringRes
    val labelId: Int
}

object Canteen : MensaAppDestination {
    override val icon = Icons.Rounded.FoodBank
    override val route = "canteen"
    override val labelId = R.string.text_canteens
}

object Cafeteria : MensaAppDestination {
    override val icon = Icons.Rounded.Coffee
    override val route = "cafeteria"
    override val labelId = R.string.text_cafes
}

object Settings: MensaAppDestination {
    override val icon = Icons.Rounded.Settings
    override val route = "settings"
    override val labelId = R.string.text_settings
}

object AdditiveWarningSettings: MensaAppDestination {
    override val icon = Icons.Rounded.Warning
    override val route = "additiveWarning"
    override val labelId = R.string.text_warnings
}

val bottomNavBarScreens = listOf(Canteen, Cafeteria, Settings)