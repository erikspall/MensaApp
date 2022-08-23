package de.erikspall.mensaapp.domain.utils

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors
import de.erikspall.mensaapp.R

object Extensions {
    @ColorInt
    fun Context.getDynamicColorIfAvailable(@AttrRes color: Int): Int {
        val dynamicColors = DynamicColors.wrapContextIfAvailable(this, R.style.Theme_MensaApp)
        val attrsToResolve = IntArray(1)
        attrsToResolve[0] = color
        val typedArray = dynamicColors.obtainStyledAttributes(attrsToResolve)
        val exColor = typedArray.getColor(0, 0)
        typedArray.recycle()
        return exColor
    }
}