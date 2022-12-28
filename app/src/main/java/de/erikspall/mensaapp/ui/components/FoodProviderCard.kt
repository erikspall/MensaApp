package de.erikspall.mensaapp.ui.components

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.ui.compose.FoodProvider
import de.erikspall.mensaapp.ui.theme.ComposeMensaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodProviderCard(
    modifier: Modifier = Modifier,
    name: String,
    type: String,
    @DrawableRes image: Int,
    openingInfo: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(28.dp))
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
    }
}

@Preview(showSystemUi = true)
@Composable
fun FoodProviderCardPreview() {
    FoodProviderCard(
        name = "Campus Hubland Nord",
        type = "Mensateria",
        image = R.drawable.mensateria_campus_hubland_nord_wuerzburg,
        openingInfo = "Öffnet in 5 min"
    )
}