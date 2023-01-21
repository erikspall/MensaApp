package de.erikspall.mensaapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.model.OpeningHour
import de.erikspall.mensaapp.domain.utils.Extensions.noRippleClickable
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*

@Composable
fun OpeningHourTile(
    modifier: Modifier = Modifier,
    smartText: String = "",
    @DrawableRes icon: Int? = null,
    openingHours: Map<DayOfWeek, List<Map<String, LocalTime>>> = emptyMap(),
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodySmall.copy(color = color),
) {
    var expanded by rememberSaveable { // Save it because it will scroll of screen
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .noRippleClickable { expanded = !expanded }
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = color
            )
        }
        if (!expanded) {
            Text(
                modifier = Modifier.padding(
                    top = 3.dp,
                    start = 8.dp,
                ),
                text = smartText,
                style = textStyle
            )
            //Spacer(modifier = Modifier.weight(1.0f))
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "",
                tint = color
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                openingHours.map { info ->
                    Row {
                        Text(
                            modifier = Modifier.padding(
                                start = 8.dp,
                            ),
                            text = info.key.getDisplayName(
                                TextStyle.FULL_STANDALONE,
                                Locale.getDefault()
                            ),
                            style = textStyle
                        )
                        Spacer(modifier = Modifier.weight(1.0f))
                        info.value.mapIndexed { index, openingInfoMap ->
                            val opens = openingInfoMap[OpeningHour.FIELD_OPENS_AT]
                            val closes = openingInfoMap[OpeningHour.FIELD_CLOSES_AT]

                            Text(
                                text = if (opens != null && closes != null)
                                    "${
                                        opens.format(
                                            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                                        )
                                    } - ${
                                        closes.format(
                                            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                                        )
                                    }"
                                else
                                    "Geschlossen",
                                style = textStyle
                            )
                            if (index != info.value.lastIndex) {
                                Text(
                                    text = " & ",
                                    style = textStyle
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewOpeningHourTile() {
    OpeningHourTile(
        icon = R.drawable.schedule,
        smartText = "Öffnet in 5 min",
        openingHours = mapOf(
            DayOfWeek.MONDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(8, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(14, 0)
                )
            ),
            DayOfWeek.TUESDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(8, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(14, 0)
                )
            ),
            DayOfWeek.WEDNESDAY to listOf(
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(8, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(12, 0)
                ),
                mapOf(
                    OpeningHour.FIELD_OPENS_AT to LocalTime.of(14, 0),
                    OpeningHour.FIELD_CLOSES_AT to LocalTime.of(16, 0)
                )
            )
        )
    )
}