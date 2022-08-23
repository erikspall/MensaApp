package de.erikspall.mensaapp.domain.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources

object HeightExtractor {
    fun getNavigationBarHeight(context: Context): Int {
        val resources: Resources = context.resources

        val resName = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            "navigation_bar_height"
        } else {
            "navigation_bar_height_landscape"
        }

        val id: Int = resources.getIdentifier(resName, "dimen", "android")

        return if (id > 0) {
            Conversion.pxToDp(resources.getDimensionPixelSize(id))
        } else {
            0
        }
    }
}