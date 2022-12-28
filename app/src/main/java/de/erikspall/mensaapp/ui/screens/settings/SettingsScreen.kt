package de.erikspall.mensaapp.ui.screens.settings

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import de.erikspall.mensaapp.ui.components.Setting
import de.erikspall.mensaapp.ui.components.SettingsList
import de.erikspall.mensaapp.ui.theme.Shrikhand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = "Einstellungen",
                        fontFamily = Shrikhand,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        content = { innerPadding ->
            SettingsList(
                modifier = modifier.padding(
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection),
                    top = innerPadding.calculateTopPadding()
                ),
                list = listOf(
                    Setting(
                        iconVector = Icons.Rounded.Person,
                        title = "Rolle",
                        subtitle = settingsViewModel.role.name
                    ),
                    Setting(
                        iconVector = Icons.Rounded.Place,
                        title = "Standort",
                        subtitle = settingsViewModel.location.name
                    ),
                    Setting(
                        iconVector = Icons.Rounded.Warning,
                        title = "Warnungen",
                        subtitle = if (settingsViewModel.warningsActivated)
                            "Warnungen sind aktiviert"
                        else
                            "Warnungen sind deaktiviert"
                    ),
                    Setting(
                        iconVector = Icons.Rounded.Info,
                        title = "Über die App",
                        subtitle = "Impressum, Datenschutz, ..."
                    )
                )
            )
        }
    )
}
