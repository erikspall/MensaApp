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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.ui.MensaViewModel
import de.erikspall.mensaapp.ui.components.Setting
import de.erikspall.mensaapp.ui.components.SettingsList
import de.erikspall.mensaapp.ui.components.SettingsRadioDialog
import de.erikspall.mensaapp.ui.theme.Shrikhand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onWarningsClicked: () -> Unit = {},
    onAboutClicked: () -> Unit = {},
    mensaViewModel: MensaViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val layoutDirection = LocalLayoutDirection.current
    val showRoleDialog = remember { mutableStateOf(false) }
    val showLocationDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.text_settings),
                        fontFamily = Shrikhand,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        content = { innerPadding ->
            if (showRoleDialog.value) {
                SettingsRadioDialog(
                    showDialog = showRoleDialog.value,
                    iconVector = Icons.Rounded.Person,
                    title = stringResource(id = R.string.text_select_role),
                    message = stringResource(id = R.string.text_select_role_description),
                    dismissText = stringResource(id = R.string.text_settings_dialog_dismiss),
                    confirmText = stringResource(id = R.string.text_settings_dialog_save),
                    items = Role.values().filter { role -> role != Role.INVALID }.toList(),
                    selectedValue = mensaViewModel.role,
                    onDismiss = {
                        showRoleDialog.value = false
                    },
                    onConfirm = { selectedStringRes ->
                        showRoleDialog.value = false
                        mensaViewModel.saveNewRole(selectedStringRes as Role)
                    }
                )
            }

            if (showLocationDialog.value) {
                SettingsRadioDialog(
                    showDialog = showLocationDialog.value,
                    iconVector = Icons.Rounded.Place,
                    title = stringResource(id = R.string.text_select_location),
                    message = stringResource(id = R.string.text_select_location_description),
                    dismissText = stringResource(id = R.string.text_settings_dialog_dismiss),
                    confirmText = stringResource(id = R.string.text_settings_dialog_save),
                    items = Location.values().toList(),
                    selectedValue = mensaViewModel.location,
                    onDismiss = {
                        showLocationDialog.value = false
                    },
                    onConfirm = { selectedStringRes ->
                        showLocationDialog.value = false
                        mensaViewModel.saveNewLocation(selectedStringRes as Location)
                    }
                )
            }

            SettingsList(
                modifier = modifier.padding(
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection),
                    top = innerPadding.calculateTopPadding()
                ),
                list = listOf(
                    Setting(
                        iconVector = Icons.Rounded.Person,
                        title = stringResource(id = R.string.text_role),
                        subtitle = stringResource(id = mensaViewModel.role.getValue()),
                        onClick = {
                            showRoleDialog.value = true
                        }
                    ),
                    Setting(
                        iconVector = Icons.Rounded.Place,
                        title = stringResource(id = R.string.text_location),
                        subtitle = stringResource(id = mensaViewModel.location.getValue()),
                        onClick = {
                            showLocationDialog.value = true
                        }
                    ),
                    Setting(
                        iconVector = Icons.Rounded.Warning,
                        title = stringResource(id = R.string.text_warnings),
                        subtitle = if (mensaViewModel.warningsActivated) {
                            stringResource(id = R.string.text_warnings_enabled)
                        } else {
                            stringResource(id = R.string.text_warnings_disabled)
                        },
                        onClick = onWarningsClicked
                    ),
                    Setting(
                        iconVector = Icons.Rounded.Info,
                        title = stringResource(id = R.string.text_about_this_app),
                        subtitle = stringResource(id = R.string.text_about_this_app_detail),
                        onClick = onAboutClicked
                    )
                )
            )
        }
    )
}
