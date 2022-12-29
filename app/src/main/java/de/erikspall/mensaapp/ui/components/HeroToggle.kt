package de.erikspall.mensaapp.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeroToggle(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean = false,
    colorChecked: Color = MaterialTheme.colorScheme.primaryContainer,
    colorUnchecked: Color = MaterialTheme.colorScheme.surfaceVariant,
    onChecked: (Boolean) -> Unit = {}
) {
    val currentColor = remember {
        Animatable(if (checked) colorChecked else colorUnchecked)
    }
    if (checked && currentColor.value != colorChecked) {
        LaunchedEffect(Unit) {
            currentColor.animateTo(colorChecked, animationSpec = tween(200))
            //color.animateTo(Color.Gray, animationSpec = tween(1000))
        }
    } else if (!checked && currentColor.value != colorUnchecked){
        LaunchedEffect(Unit) {
            currentColor.animateTo(colorUnchecked, animationSpec = tween(200))
        }

    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(corner = CornerSize(28.dp)),
        colors = CardDefaults.cardColors(
            containerColor = currentColor.value
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1.0f)) // fill height with spacer
            Switch(
                modifier = Modifier
                    .padding(end = 24.dp),
                checked = checked,
                thumbContent = {
                    if (checked) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "",
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    }
                },
                onCheckedChange = {
                    onChecked(it)
                })
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewHeroToggle() {
    val switchChecked = remember {
        mutableStateOf(false)
    }
    HeroToggle(title = "Test", checked = switchChecked.value, onChecked = {
        switchChecked.value = it
    })
}

