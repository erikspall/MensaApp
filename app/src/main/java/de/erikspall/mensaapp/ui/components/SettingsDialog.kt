package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.erikspall.mensaapp.domain.enums.Location

@Composable
fun SettingsRadioDialog(
    iconVector: ImageVector,
    title: String,
    message: String,
    extraContent:  @Composable () -> Unit = {},
    dismissText: String,
    confirmText: String,
    onConfirm: () -> Unit = {}
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            icon = { Icon(iconVector, contentDescription = null) },
            title = {
                Text(text = title)
            },
            text = {
                Column {
                    Text(text = message)
                    Divider(
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                    )
                    extraContent()
                    Divider(
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onConfirm()
                    }
                ) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(dismissText)
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewSettingsRadioDialog() {
    SettingsRadioDialog(
        iconVector = Icons.Rounded.Place,
        title = "Standort wählen",
        confirmText = "Speichern",
        dismissText = "Zurück",
        message = "Wähle hier welchen Standort du sehen möchtest",
        extraContent = {
            val selected = remember { mutableStateOf(Location.WUERZBURG) }
            Column {
                for (location in Location.values()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (selected.value == location),
                                onClick = { selected.value = location },
                                role = Role.RadioButton
                            )
                            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                    ) {
                        RadioButton(
                            selected = (selected.value == location),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(id = location.getValue()),
                        )
                    }
                }
            }

        }
    )
}