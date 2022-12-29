package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsList(
    modifier: Modifier = Modifier,
    list: List<Setting>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(list) { setting ->
            SettingsItem(
                iconVector = setting.iconVector,
                title = setting.title,
                subtitle = setting.subtitle,
                onClick = setting.onClick
            )
        }
    }
}

data class Setting(
    val iconVector: ImageVector,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit = {}
)

@Preview(showSystemUi = true)
@Composable
fun PreviewSettingsList() {
    SettingsList(list = listOf(
        Setting(
            iconVector = Icons.Rounded.Person,
            title = "Rolle",
            subtitle = "Student"
        ),
        Setting(
            iconVector = Icons.Rounded.Place,
            title = "Standort",
            subtitle = "Würzburg"
        ),
        Setting(
            iconVector = Icons.Rounded.Warning,
            title = "Warnungen",
            subtitle = "Warnungen sind deaktiviert"
        ),
        Setting(
            iconVector = Icons.Rounded.Info,
            title = "Über die App",
            subtitle = "Impressum, Datenschutz, ..."
        )
    ))
}
