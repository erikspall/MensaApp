package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Blender
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import de.erikspall.mensaapp.domain.model.Additive
import java.util.logging.Filter
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditiveFilterSection(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    sectionTitle: String,
    items: List<Additive>
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = icon,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = sectionTitle,
                style = MaterialTheme.typography.titleMedium
            )

        }
        FlowRow(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            //crossAxisSpacing = 16.dp,
            mainAxisSpacing = 8.dp
        ) {
            items.forEach {
                FilterChip(
                    selected = it.isNotLiked,
                    leadingIcon = {
                        if (it.isNotLiked)
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Rounded.Check,
                                contentDescription = ""
                            )
                    },
                    onClick = { },
                    label = {
                        Text(text = it.name)
                    }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAdditiveFilterSection() {


    AdditiveFilterSection(
        icon = Icons.Rounded.Blender, sectionTitle = "Zutaten", items = listOf(
            Additive(name = "Additive", isNotLiked = Random.nextBoolean()),
            Additive(name = "Additive", isNotLiked = Random.nextBoolean()),
            Additive(name = "Additive", isNotLiked = Random.nextBoolean()),
            Additive(name = "Additive", isNotLiked = Random.nextBoolean()),
            Additive(name = "Additive", isNotLiked = Random.nextBoolean()),
            Additive(name = "Additive", isNotLiked = Random.nextBoolean()),
            Additive(name = "Additive", isNotLiked = Random.nextBoolean()),
            Additive(name = "Additive", isNotLiked = Random.nextBoolean()),
            Additive(name = "Additive", isNotLiked = Random.nextBoolean())

        )
    )
}