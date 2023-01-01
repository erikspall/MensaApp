package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.model.FoodProvider

@Composable
fun DetailHeader(
    modifier: Modifier = Modifier,
    foodProvider: FoodProvider
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
       /* FilledIconButton(
            modifier = Modifier

                .zIndex(1f),
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = ""
            )
        }*/
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .height(230.dp)
                .zIndex(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            DetailChip(
                iconVector = Icons.Rounded.Place,
                text = foodProvider.location
            )
            Spacer(modifier = Modifier.height(8.dp))
            DetailChip(
                iconVector = Icons.Rounded.Schedule,
                text = foodProvider.openingHoursString
            )
        }

        Column {
            Image(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                ,
                painter = painterResource(id = foodProvider.photo),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .padding(top = 24.dp, start = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = foodProvider.name,
                    //fontFamily = Shrikhand,
                    style = MaterialTheme.typography.headlineLarge
                )
                IconToggleButton(
                    modifier = Modifier.padding(end = 20.dp),
                    checked = true,
                    onCheckedChange = { }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDetailHeader() {
    DetailHeader(foodProvider = FoodProvider(
        name = "Campus Hubland Nord",
        photo = R.drawable.mensateria_campus_hubland_nord_wuerzburg,
        openingHoursString = "Öffnet in 5 min",
        location = "Würzburg"
    ))
}