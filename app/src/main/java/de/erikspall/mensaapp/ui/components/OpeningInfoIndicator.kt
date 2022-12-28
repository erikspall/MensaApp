package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OpeningInfoIndicator(
    modifier: Modifier = Modifier,
    info: String
) {
    Row(
        modifier = modifier
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Rounded.Schedule,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.tertiary
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp)
            ,
            text = info,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview
@Composable
fun PreviewOpeningInfoIndicator() {
    OpeningInfoIndicator(info = "Öffnet in 5 Min.")
}