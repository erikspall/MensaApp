package de.erikspall.mensaapp.domain.utils

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors
import de.erikspall.mensaapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList

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

    fun View.pushContentUpBy(dp: Int = 50){
        this.setPadding(8, 0, 8, Conversion.dpToPx(dp))
    }

    fun LinearLayoutCompat.pushContentUpBy(
        pushDp: Int = 50
    ) {
        this.setPaddingRelative(paddingStart,paddingTop,paddingEnd, Conversion.dpToPx(pushDp))
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()
}