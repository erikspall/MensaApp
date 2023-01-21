package de.erikspall.mensaapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.utils.Extensions.noRippleClickable

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontStyle: FontStyle? = null,
    text: String,
    @DrawableRes icon: Int? = null,
    iconTint: Color = style.color,
    collapsedMaxLine: Int = 1,
    showMoreText: String = "... show more",
    showLessText: String = "show less",
    showMoreStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.W500,
        color = MaterialTheme.colorScheme.tertiary
    ),
    showLessStyle: SpanStyle = showMoreStyle,
    textAlign: TextAlign? = null,
    isExpanded: Boolean = false,
    clickable: Boolean = false,
    lastCharIndex: Int = 0,
    onClick: () -> Unit = {},
    onTextLayout: (TextLayoutResult, Int) -> Unit = { _, _ -> }
) {
    Box(
        modifier = Modifier
            .noRippleClickable {
                onClick()
            }
            .then(modifier)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (icon != null) {
                Icon(painter = painterResource(id = icon), contentDescription = "", tint = iconTint)
            }
            Text(
                modifier = textModifier
                    .padding(top = 3.dp)
                    .fillMaxWidth()
                    .animateContentSize(),
                text = buildAnnotatedString {
                    if (clickable) {
                        if (isExpanded) {
                            append(text)
                            withStyle(style = showLessStyle) { append(" $showLessText") }
                        } else {
                            val adjustText =
                                text.substring(startIndex = 0, endIndex = lastCharIndex)
                                    .dropLast(showMoreText.length)
                                    .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                            append(adjustText)
                            withStyle(style = showMoreStyle) { append(showMoreText) }
                        }
                    } else {
                        append(text)
                    }
                },
                maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
                fontStyle = fontStyle,
                onTextLayout = { textLayoutResult ->
                    onTextLayout(textLayoutResult, collapsedMaxLine)
                },
                style = style,
                textAlign = textAlign
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewExpandableText() {
    val expandableTextState = remember {
        ExpandableTextState()
    }

    ExpandableText(
        text = LoremIpsum(100).values.joinToString(),
        style = MaterialTheme.typography.bodyMedium,
        isExpanded = expandableTextState.isExpanded,
        clickable = expandableTextState.clickable,
        icon = R.drawable.schedule,
        lastCharIndex = expandableTextState.lastCharIndex,
        showLessText = " weniger",
        showMoreText = "... mehr",
        onClick = {
            expandableTextState.isExpanded = !expandableTextState.isExpanded
        },
        onTextLayout = { textLayoutResult, collapsedMaxLines ->
            if (!expandableTextState.isExpanded && textLayoutResult.hasVisualOverflow) {
                expandableTextState.clickable = true
                expandableTextState.lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLines - 1)
            }
        }
    )
}

class ExpandableTextState {
    var isExpanded by mutableStateOf(false)
    var clickable by mutableStateOf(false)
    var lastCharIndex by mutableStateOf(0)
}