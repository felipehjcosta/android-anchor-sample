package com.github.felipehjcosta.androidanchorsample

import android.view.View
import android.view.ViewGroup

class AnchorBehaviorStrategy(
        private val anchor: View,
        private val placeHolder: View
) : ObservableScrollView.ObservableScrollViewCallbacks {

    override fun onScrollChanged(observableScrollView: ObservableScrollView, deltaY: Int) {
        val scrollViewTopOnScreen = calculateScrollViewTopOnScreen(observableScrollView)
        setupPivot()
        translateVertically(scrollViewTopOnScreen)
        scaleBasedOnHeight()
    }

    private fun setupPivot() {
        anchor.pivotX = anchor.width / 2.0f
        anchor.pivotY = anchor.height / 2.0f
    }

    private fun translateVertically(scrollViewTopOnScreen: Int) {
        val translation = when {
            isPlaceHolderBelowScrollViewport() -> (anchor.parent as ViewGroup).height - anchor.height
            isPlaceHolderAboveScrollViewport() -> placeHolderTopOnScreen - scrollViewTopOnScreen
            else -> {
                val anchorTop = screenHeight - anchor.height
                val delta = if (placeHolderTopOnScreen > anchorTop) {
                    placeHolderTopOnScreen - anchorTop
                } else {
                    0
                }
                placeHolderTopOnScreen - scrollViewTopOnScreen - delta
            }
        }

        anchor.translationY = translation.toFloat()
    }

    private fun scaleBasedOnHeight() {
        val scale = when {
            isPlaceHolderBelowScrollViewport() -> 1.0f
            isPlaceHolderAboveScrollViewport() -> 1.0f
            else -> {
                val anchorTop = screenHeight - anchor.height
                val delta = if (placeHolderTopOnScreen > anchorTop) {
                    placeHolderTopOnScreen - anchorTop
                } else {
                    0
                }

                val scale = delta.toFloat() / anchor.height
                if (scale < 0.95f) 0.95f else scale
            }
        }

        anchor.apply {
            scaleX = scale
            scaleY = scale
        }
    }

    private val placeHolderTopOnScreen
        get() = with(intArrayOf(0, 0)) {
            placeHolder.getLocationOnScreen(this)
            this[1]
        }

    private val screenHeight: Int
        get() = anchor.resources.displayMetrics.heightPixels

    private fun calculateScrollViewTopOnScreen(scrollView: ObservableScrollView): Int = with(intArrayOf(0, 0)) {
        scrollView.getLocationOnScreen(this)
        this[1]
    }

    private fun isPlaceHolderBelowScrollViewport() = placeHolderTopOnScreen > screenHeight

    private fun isPlaceHolderAboveScrollViewport() = placeHolderTopOnScreen < 0
}