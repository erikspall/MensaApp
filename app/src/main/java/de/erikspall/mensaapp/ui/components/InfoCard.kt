package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit = {},
    onButtonClick: () -> Unit = {},
    icon: ImageVector? = null,
    buttonText: String? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(28.dp)), // TODO: Not needed
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null)
                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        imageVector = icon,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
                )
            }
            content()
            if (buttonText != null)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = onButtonClick
                    ) {
                        Text(text = buttonText)
                    }
                }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewInfoCard() {
    InfoCard(
        icon = Icons.Rounded.Code,
        title = "Test",
        content = {
                  Text(
                      text = "Jo also ich bin der Text"
                  )
        },
        buttonText = "Github öffnen"
    )
}