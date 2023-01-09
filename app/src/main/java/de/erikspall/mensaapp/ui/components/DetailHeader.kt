package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.model.FoodProvider

@Composable
fun DetailHeader(
    modifier: Modifier = Modifier,
    foodProvider: FoodProvider,
    chipScrollState: ScrollState,
    descriptionState: ExpandableTextState? = null,
    additionalInfoState: ExpandableTextState? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CornerSize(28.dp))),
                painter = painterResource(id = foodProvider.photo),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            DetailChip(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                text = foodProvider.type,
                orientation = ChipOrientation.END
            )
            DetailChip(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                text = foodProvider.openingHoursString,
                orientation = ChipOrientation.START
            )
        }

        Column(
            modifier = Modifier.padding(start = 40.dp, end = 40.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (descriptionState != null && foodProvider.description.isNotEmpty()) {
                ExpandableText(
                    text = foodProvider.description,
                    showMoreText = stringResource(id = R.string.text_label_read_more),
                    showLessText = stringResource(id = R.string.text_label_read_less),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    isExpanded = descriptionState.isExpanded,
                    clickable = descriptionState.clickable,
                    icon = Icons.Outlined.Info,
                    lastCharIndex = descriptionState.lastCharIndex,
                    onClick = {
                        descriptionState.isExpanded = !descriptionState.isExpanded
                    },
                    onTextLayout = { textLayoutResult, collapsedMaxLines ->
                        if (!descriptionState.isExpanded && textLayoutResult.hasVisualOverflow) {
                            descriptionState.clickable = true
                            descriptionState.lastCharIndex =
                                textLayoutResult.getLineEnd(collapsedMaxLines - 1)
                        }
                    }
                )
            }

            if (additionalInfoState != null && foodProvider.additionalInfo.isNotEmpty()) {
                ExpandableText(
                    text = foodProvider.additionalInfo,
                    showMoreText = stringResource(id = R.string.text_label_read_more),
                    showLessText = stringResource(id = R.string.text_label_read_less),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    isExpanded = additionalInfoState.isExpanded,
                    clickable = additionalInfoState.clickable,
                    icon = Icons.Rounded.Store,
                    lastCharIndex = additionalInfoState.lastCharIndex,
                    onClick = {
                        additionalInfoState.isExpanded = !additionalInfoState.isExpanded
                    },
                    onTextLayout = { textLayoutResult, collapsedMaxLines ->
                        if (!additionalInfoState.isExpanded && textLayoutResult.hasVisualOverflow) {
                            additionalInfoState.clickable = true
                            additionalInfoState.lastCharIndex =
                                textLayoutResult.getLineEnd(collapsedMaxLines - 1)
                        }
                    }
                )
            }
        }
    }




}

@Preview(showSystemUi = true)
@Composable
fun PreviewDetailHeader() {
    val chipScrollState = rememberScrollState()
    val descriptionState = remember {
        ExpandableTextState()
    }
    val additionalInfoState = remember {
        ExpandableTextState()
    }

    DetailHeader(
        foodProvider = FoodProvider(
            name = "Campus Hubland Nord",
            photo = R.drawable.mensateria_campus_hubland_nord_wuerzburg,
            openingHoursString = "Öffnet in 5 min",
            location = "Würzburg",
            type = "Mensateria",
            additionalInfo = "Die Abendmensa hat geschlossen!",
            description = "Die Mensateria mit 500 Innen- und 60 Balkonplätzen befindet sich im Obergeschoss des Gebäudes am westlichen Rand des Campus Nord am Hubland. Zur Südseite hin trägt die Mensateria einen Balkon, so dass ein Aufenthalt im Freien mit Blick auf den Hubland-Campus möglich ist.\n"
                    + "\nAusgestattet ist die Mensateria mit einem modernen Speiseleitsystem sowie speziellen Angebotsvarianten."
        ),
        chipScrollState = chipScrollState,
        descriptionState = descriptionState,
        additionalInfoState = additionalInfoState
    )
}