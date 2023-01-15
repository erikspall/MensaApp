package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TypeChip(
    modifier: Modifier = Modifier,
    text: String
) {
    Surface(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer, shape =  RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 2.dp
                ))
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer),
            text = text,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
fun PreviewTypeChip() {
    TypeChip(
        text = "Mensateria"
    )
}