package de.erikspall.mensaapp.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.FoodBank
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

interface MensaAppDestination {
    val icon: ImageVector
    val route: String
    val label: String
}

object Canteen : MensaAppDestination {
    override val icon = Icons.Rounded.FoodBank
    override val route = "canteen"
    override val label = "Mensen"
}

object Cafeteria : MensaAppDestination {
    override val icon = Icons.Rounded.Coffee
    override val route = "cafeteria"
    override val label = "Cafeterien"
}

object Settings: MensaAppDestination {
    override val icon = Icons.Rounded.Settings
    override val route = "settings"
    override val label = "Einstellungen"
}

object FoodProvider

val bottomNavBarScreens = listOf(Canteen, Cafeteria, Settings)