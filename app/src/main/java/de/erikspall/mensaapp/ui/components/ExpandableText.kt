package de.erikspall.mensaapp.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

/*
@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    minimizedMaxLines: Int,
    style: TextStyle
) {
    var expanded by remember { mutableStateOf(false) }
    var hasVisualOverflow by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            onTextLayout = { hasVisualOverflow = it.hasVisualOverflow },
            style = style
        )
        if (hasVisualOverflow) {
            Row(
                modifier = Modifier.align(Alignment.BottomEnd),
                verticalAlignment = Alignment.Bottom
            ) {
                val lineHeightDp: Dp = with(LocalDensity.current) { style.lineHeight.toDp() }
                Spacer(
                    modifier = Modifier
                        .width(48.dp)
                        .height(lineHeightDp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )
                Text(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(start = 4.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { expanded = !expanded }
                        ),
                    text = "Show More",
                    color = MaterialTheme.colorScheme.primary,
                    style = style
                )
            }
        }
    }
}
*/

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontStyle: FontStyle? = null,
    text: String,
    collapsedMaxLine: Int = 1,
    showMoreText: String = "... show more",
    showLessText: String = "show less",
    showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500, color = MaterialTheme.colorScheme.primary),
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
            .clickable(clickable) {
                onClick()
            }
            .then(modifier)
    ) {
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildAnnotatedString {
                if (clickable) {
                    if (isExpanded) {
                        append(text)
                        withStyle(style = showLessStyle) { append(showLessText) }
                    } else {
                        val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
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

@Preview(showBackground = true)
@Composable
fun PreviewExpandableText() {
    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableStateOf(0) }

    ExpandableText(
        text = LoremIpsum(100).values.joinToString(),
        style = MaterialTheme.typography.bodyMedium,
        isExpanded = isExpanded,
        clickable = clickable,
        lastCharIndex = lastCharIndex,
        showLessText = " weniger",
        showMoreText = "... mehr",
        onClick = {
            isExpanded = !isExpanded
        },
        onTextLayout = { textLayoutResult, collapsedMaxLines ->
            if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                clickable = true
                lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLines - 1)
            }
        }
    )
}