package de.erikspall.mensaapp.ui.foodproviderdetail.behavior

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewPropertyAnimator
import androidx.annotation.Dimension
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

class HideBackButtonOnScrollBehavior<V : View> : CoordinatorLayout.Behavior<V> {
    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    interface OnScrollStateChangedListener {
        /**
         * Called when the bottom view changes its scrolled state.
         *
         * @param bottomView The bottom view.
         * @param newState The new state. This will be one of [.STATE_SCROLLED_UP] or [     ][.STATE_SCROLLED_DOWN].
         */
        fun onStateChanged(
            bottomView: View,
            newState: Int
        )
    }

    private val onScrollStateChangedListeners = LinkedHashSet<OnScrollStateChangedListener>()

    private val STATE_SCROLLED_DOWN = 1
    private val STATE_SCROLLED_UP = 2

    protected val ENTER_ANIMATION_DURATION = 225
    protected val EXIT_ANIMATION_DURATION = 175

    private var currentState = STATE_SCROLLED_DOWN
    private var currentAnimator: ViewPropertyAnimator? = null

    private var additionalHiddenOffsetY = 0
    private var height = 0

    fun setAdditionalHiddenOffsetY(child: V, @Dimension offset: Int) {
        additionalHiddenOffsetY = offset
        if (currentState == STATE_SCROLLED_DOWN) {
            child.translationY = (height - additionalHiddenOffsetY).toFloat()
        }
    }

    @Override
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    @Override
    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        val paramsCompat = child.layoutParams as MarginLayoutParams
        height = child.measuredHeight + paramsCompat.topMargin
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    @Override
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed > 0) {
            slideUp(child, true)
        } else if (dyConsumed < 0) {
            slideDown(child, true)
        }
    }

    fun isScrolledUp(): Boolean {
        return currentState == STATE_SCROLLED_UP
    }

    fun slideUp(child: V, animate: Boolean) {
        if (isScrolledUp()) {
            return
        }
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }
        updateCurrentState(child, STATE_SCROLLED_UP)
        val targetTranslationY = -height
        if (animate) {
            animateChildTo(
                child,
                targetTranslationY,
                ENTER_ANIMATION_DURATION.toLong(),
                LinearOutSlowInInterpolator()
            )
        } else {
            child.translationY = targetTranslationY.toFloat()
        }
    }

    fun slideDown(child: V, animate: Boolean) {
        if (isScrolledDown()) {
            return
        }
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }
        updateCurrentState(child, STATE_SCROLLED_DOWN)
        val targetTranslationY: Int = 0
        if (animate) {
            animateChildTo(
                child,
                targetTranslationY,
                EXIT_ANIMATION_DURATION.toLong(),
                FastOutLinearInInterpolator()
            )
        } else {
            child.translationY = targetTranslationY.toFloat()
        }
    }

    fun isScrolledDown(): Boolean {
        return currentState == STATE_SCROLLED_DOWN
    }

    private fun updateCurrentState(
        child: V,
        state: Int
    ) {
        currentState = state
        for (listener in onScrollStateChangedListeners) {
            listener.onStateChanged(child, currentState)
        }
    }

    private fun animateChildTo(
        child: V, targetY: Int, duration: Long, interpolator: TimeInterpolator
    ) {
        currentAnimator = child
            .animate()
            .translationY(targetY.toFloat())
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimator = null
                    }
                })
    }

    fun addOnScrollStateChangedListener(listener: OnScrollStateChangedListener) {
        onScrollStateChangedListeners.add(listener)
    }


    fun removeOnScrollStateChangedListener(listener: OnScrollStateChangedListener) {
        onScrollStateChangedListeners.remove(listener)
    }

    fun clearOnScrollStateChangedListeners() {
        onScrollStateChangedListeners.clear()
    }
}
