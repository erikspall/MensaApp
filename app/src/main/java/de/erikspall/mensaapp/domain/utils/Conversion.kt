package de.erikspall.mensaapp.domain.utils

import android.content.res.Resources

object Conversion {
    fun pxToDp(pixel: Int): Int = (pixel / Resources.getSystem().displayMetrics.density).toInt()

    fun dpToPx(dp: Int): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()
}