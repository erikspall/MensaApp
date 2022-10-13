package de.erikspall.mensaapp.domain.utils

import android.content.Context
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.text.HtmlCompat
import com.google.android.material.textview.MaterialTextView
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.utils.Extensions.getDynamicColorIfAvailable
import kotlin.math.roundToInt

open class NoUnderlineClickSpan(val context: Context) : ClickableSpan() {
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = false
        textPaint.color = context.getDynamicColorIfAvailable(R.attr.colorPrimary)
    }

    override fun onClick(widget: View) {}
}

object MaterialTextViewExtension {
    fun MaterialTextView.setTextWithLineConstraint(
        fullText: String,
        maxLines: Int,
        viewMore: Boolean = true,
        applyExtraHighlights: ((Spannable) -> (Spannable))? = null
    ) {
        val width = this.width
        if (width <= 0) {
            post {
                this.setTextWithLineConstraint( // ???
                    fullText = fullText,
                    maxLines = maxLines,
                    viewMore = viewMore,
                    applyExtraHighlights = applyExtraHighlights
                )
            }
            return
        }
        this.movementMethod = LinkMovementMethod.getInstance()

        // Since we take the string character by character, we don't want to break up the
        // Windows-style line endings.
        val adjustedText = fullText.replace("\r\n", "\n")

        // Check if text has to be resizable
        val textLayout = StaticLayout.Builder.obtain(
            fullText,
            0,
            adjustedText.length,
            this.paint,
            this.width - this.paddingStart - this.paddingEnd
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(this.lineSpacingExtra, this.lineSpacingMultiplier)
            .setIncludePad(this.includeFontPadding)
            .build()

        if (textLayout.lineCount <= maxLines) {
           this.text = fullText
           return
        }

        val charsAtLineEnd = textLayout.getLineEnd(maxLines - 1)
        val suffixText =
            if (viewMore)
                this.resources.getString(R.string.resizable_text_read_more)
            else
                "\u00A0${this.context.getString(R.string.resizable_text_read_less)}"

        var charsToTake = charsAtLineEnd - suffixText.length / 2

        if (charsToTake <= 0) {
            // Happens when text is empty
            val htmlText = adjustedText.replace("\n", "<br/>")
            this.text = this.addTextResizable(
                fullText = fullText,
                maxLines = maxLines,
                shortenedText = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                buttonText = null,
                viewMore = viewMore,
                applyExtraHighlights = applyExtraHighlights
            )
            return
        }
        if (!viewMore) {
            // We can set the text immediately, nothing needs to be measured
            val htmlText = adjustedText.replace("\n", "<br/>")
            this.text = this.addTextResizable(
                fullText = fullText,
                maxLines = maxLines,
                shortenedText = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                buttonText = suffixText,
                viewMore = viewMore,
                applyExtraHighlights = applyExtraHighlights
            )
            return
        }

        val lastHasNewLine = adjustedText.substring(
            textLayout.getLineStart(maxLines - 1),
            textLayout.getLineEnd(maxLines - 1)
        ).contains("\n")
        var linedText = if (lastHasNewLine) {
            val charsPerLine = textLayout.getLineEnd(0).toDouble() / (textLayout.getLineWidth(0)
                .toDouble() / textLayout.ellipsizedWidth.toDouble())
            val lineOfSpaces =
                "\u00A0".repeat(charsPerLine.roundToInt()) // non breaking space, will not be thrown away by HTML parser
            charsToTake += lineOfSpaces.length - 1
            adjustedText.take(textLayout.getLineStart(maxLines - 1)) + adjustedText.substring(
                textLayout.getLineStart(maxLines - 1),
                textLayout.getLineEnd(maxLines - 1)
            )
                .replace(
                    "\n",
                    lineOfSpaces
                ) + adjustedText.substring(textLayout.getLineEnd(maxLines - 1))
        } else {
            adjustedText
        }
        // Check if we perhaps need to even add characters? Happens very rarely, but can be possible if there was a long word just wrapped
        val shortenedString = linedText.take(charsToTake)
        val shortenedStringWithSuffix = shortenedString + suffixText
        val shortenedStringWithSuffixLayout = StaticLayout.Builder.obtain(
            shortenedStringWithSuffix,
            0,
            shortenedStringWithSuffix.length,
            this.paint,
            this.width - this.paddingStart - this.paddingEnd,
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(this.lineSpacingExtra, this.lineSpacingMultiplier)
            .setIncludePad(this.includeFontPadding)
            .build()

        val modifier: Int
        if (shortenedStringWithSuffixLayout.getLineEnd(maxLines - 1) >= shortenedStringWithSuffix.length) {
            modifier = 1
            charsToTake--
        } else {
            modifier = -1
        }

        do {
            charsToTake += modifier
            val baseString = linedText.take(charsToTake)
            val appended = baseString + suffixText
            val newLayout = StaticLayout.Builder.obtain(
                appended,
                0,
                appended.length,
                this.paint,
                this.width - this.paddingStart - this.paddingEnd,
            )
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(this.lineSpacingExtra, this.lineSpacingMultiplier)
                .setIncludePad(this.includeFontPadding)
                .build()
        } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length) ||
            (modifier > 0 && newLayout.getLineEnd(maxLines - 1) >= appended.length)
        )
        if (modifier > 0) {
            charsToTake-- // We went overboard with 1 char, fixing that
        }
        // We need to convert newlines because we are going over to HTML now
        val htmlText = linedText.take(charsToTake).replace("\n", "<br/>").trim()
        this.text = this.addTextResizable(
            fullText = fullText,
            maxLines = maxLines,
            shortenedText = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            buttonText = suffixText,
            viewMore = viewMore,
            applyExtraHighlights = applyExtraHighlights
        )
    }

    private fun MaterialTextView.addTextResizable(
        fullText: String,
        maxLines: Int,
        shortenedText: Spanned,
        buttonText: String?,
        viewMore: Boolean = true,
        applyExtraHighlights: ((Spannable) -> Spannable)? = null
    ): Spannable {
        val builder = SpannableStringBuilder(shortenedText)
        if (buttonText != null) {
            builder.append(buttonText)
            // Don't highlight "... "
            val startIndexOffset = if (viewMore) buttonText.indexOfFirst { c -> c == ' ' } else 0
            builder.setSpan(
                object : NoUnderlineClickSpan(this.context) {},
                builder.indexOf(buttonText) + startIndexOffset,
                builder.indexOf(buttonText) + buttonText.length,
                0
            )
            this.setOnClickListener {
                if (viewMore) {
                    setTextWithLineConstraint(fullText, maxLines, false, applyExtraHighlights)
                } else {
                    setTextWithLineConstraint(fullText, maxLines, true, applyExtraHighlights)
                }
            }
        }
        if (applyExtraHighlights != null) {
            return applyExtraHighlights(builder)
        }
        return builder
    }
}