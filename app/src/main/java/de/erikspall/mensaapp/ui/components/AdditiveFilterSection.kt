package de.erikspall.mensaapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.model.Additive
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditiveFilterSection(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    sectionTitle: String,
    items: List<Additive>,
    onAdditiveClicked: (Additive) -> Unit = {}
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
                painter = painterResource(id = icon),
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
                    onClick = {onAdditiveClicked(it)},
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
        icon = R.drawable.blender, sectionTitle = "Zutaten", items = listOf(
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