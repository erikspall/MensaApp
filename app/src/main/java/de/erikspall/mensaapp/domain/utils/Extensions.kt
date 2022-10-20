package de.erikspall.mensaapp.domain.utils

import android.content.Context
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.color.DynamicColors
import de.erikspall.mensaapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object Extensions {
    @ColorInt
    fun Context.getDynamicColorIfAvailable(@AttrRes color: Int): Int {

        val dynamicColors =
            if (!DynamicColors.isDynamicColorAvailable()) DynamicColors.wrapContextIfAvailable(
                this,
                R.style.Theme_MensaApp
            )
            else DynamicColors.wrapContextIfAvailable(
                this,
                com.google.android.material.R.style.ThemeOverlay_Material3_DynamicColors_DayNight
            )

        val attrsToResolve = IntArray(1)
        attrsToResolve[0] = color
        val typedArray = dynamicColors.obtainStyledAttributes(attrsToResolve)
        val exColor = typedArray.getColor(0, 0)
        typedArray.recycle()
        return exColor
    }

    fun View.pushContentUpBy(dp: Int = 50) {
        this.setPadding(8, 0, 8, Conversion.dpToPx(dp))
    }

    fun LinearLayoutCompat.pushContentUpBy(
        pushDp: Int = 50
    ) {
        this.setPaddingRelative(paddingStart, paddingTop, paddingEnd, Conversion.dpToPx(pushDp))
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    suspend fun <T> Flow<List<T>>.flattenToList() = flatMapConcat { it.asFlow() }.toList()

    fun LocalDate.toDate(): Date {
        return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    /*fun MaterialTextView.setTextWithLineConstraint(
        text: String, maxLines: Int, truncated: Boolean = true
    ) {
        this.text = text
        this.post {
            if (this.lineCount > maxLines) {
                // We have to consider "\n", so remove them, and reconstruct later

                val lastCharShown = this.layout.getLineVisibleEnd(maxLines - 1)


                if (truncated) this.maxLines = maxLines else this.maxLines = Int.MAX_VALUE

                val appendString = context.getString(
                    if (truncated)
                        R.string.resizable_text_read_more
                    else
                        R.string.resizable_text_read_less
                )

                val suffix = "  $appendString" // 2 spaces

                val ellipses = if (truncated) "..." else ""

                val actionDisplayText = if (truncated)
                    text
                        .substring(0, lastCharShown - suffix.length - ellipses.length) +
                            ellipses +
                            suffix
                else
                    text + ellipses + suffix

                val truncatedSpannableString = SpannableString(actionDisplayText)
                val startIndex = actionDisplayText.indexOf(appendString)
                truncatedSpannableString.setSpan(
                    ForegroundColorSpan(
                        context.getDynamicColorIfAvailable(
                            R.attr.colorPrimary
                        )
                    ),
                    startIndex,
                    startIndex + appendString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                this.text = truncatedSpannableString

                this.setOnClickListener {
                    this.setTextWithLineConstraint(text, maxLines, !truncated)
                }
            }
        }
    }*/
}
