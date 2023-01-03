package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DetailChip(
    modifier: Modifier = Modifier,
    iconVector: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onContainerColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    orientation: ChipOrientation = ChipOrientation.START,
    text: String
) {
    Surface(
        modifier = modifier
            .background(
                containerColor, shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (orientation == ChipOrientation.START) 2.dp else 16.dp,
                    bottomEnd = if (orientation == ChipOrientation.END) 2.dp else 16.dp
                )
            )
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.background(color = containerColor),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (iconVector != null) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = iconVector,
                    contentDescription = "",
                    tint = onContainerColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = onContainerColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
fun PreviewDetailChip() {
    DetailChip(
        text = "Mensateria",
        iconVector = Icons.Rounded.Schedule
    )
}

enum class ChipOrientation{
    START,
    END
}
