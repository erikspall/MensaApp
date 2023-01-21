package de.erikspall.mensaapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.erikspall.mensaapp.R

@Composable
fun DetailChip(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (iconRes != null) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = iconRes),
                    contentDescription = "",
                    tint = onContainerColor
                )
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
        iconRes = R.drawable.schedule

    )
}

enum class ChipOrientation{
    START,
    END
}
