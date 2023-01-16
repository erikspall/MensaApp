package de.erikspall.mensaapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.utils.Extensions.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodProviderCard(
    modifier: Modifier = Modifier,
    name: String,
    type: String,
    @DrawableRes image: Int,
    openingInfo: String,
    liked: Boolean = false,
    onPressed: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                onPressed()
            }
            .wrapContentHeight()
            .clip(RoundedCornerShape(28.dp)), // TODO: Not needed
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                TypeChip(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 12.dp, top = 12.dp),
                    text = type
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    OpeningInfoIndicator(
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp),
                        info = openingInfo
                    )
                }
                if (liked) {
                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FoodProviderCardPreview() {
    FoodProviderCard(
        name = "Campus Hubland Nord",
        type = "Mensateria",
        image = R.drawable.mensateria_campus_hubland_nord_wuerzburg,
        openingInfo = "Öffnet in 5 min",
        liked = true
    )
}