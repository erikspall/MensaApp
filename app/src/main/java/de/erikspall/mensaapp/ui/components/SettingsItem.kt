package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    iconVector: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }, // TODO: Can this be done better?
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        Icon(
            imageVector = iconVector,
            modifier = Modifier
                .size(24.dp),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground
        )
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
        Spacer(modifier = Modifier.width(24.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSettingsItem() {
    SettingsItem(
        iconVector = Icons.Rounded.AccountCircle,
        title = "Rolle",
        subtitle = "Student"
    )
}