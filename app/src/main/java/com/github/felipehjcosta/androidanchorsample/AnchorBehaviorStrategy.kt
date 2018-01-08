package com.github.felipehjcosta.androidanchorsample

import android.view.View
import android.view.ViewGroup

class AnchorBehaviorStrategy(
        private val anchor: View,
        private val placeHolder: View
) : ObservableScrollView.ObservableScrollViewCallbacks {

    override fun onScrollChanged(observableScrollView: ObservableScrollView, deltaY: Int) {

        val topOffset = calculateScrollViewTop(observableScrollView)

        anchor.translationY = calculateVerticalTranslation(topOffset).toFloat()
    }

    private fun calculateVerticalTranslation(topOffset: Int): Int = when {
        isPlaceHolderBelowScrollViewport() -> (anchor.parent as ViewGroup).height - anchor.height
        isPlaceHolderAboveScrollViewport() -> placeHolderYPositionOnScreen - topOffset
        else -> {
            val bottomLocation = screenHeight - anchor.height
            val delta = if (placeHolderYPositionOnScreen > bottomLocation) {
                placeHolderYPositionOnScreen - bottomLocation
            } else {
                0
            }
            placeHolderYPositionOnScreen - topOffset - delta
        }
    }

    private val placeHolderYPositionOnScreen
        get() = with(intArrayOf(0, 0)) {
            placeHolder.getLocationOnScreen(this)
            this[1]
        }

    private val screenHeight: Int
        get() = anchor.resources.displayMetrics.heightPixels

    private fun calculateScrollViewTop(scrollView: ObservableScrollView): Int = with(intArrayOf(0, 0)) {
        scrollView.getLocationOnScreen(this)
        this[1]
    }

    private fun isPlaceHolderBelowScrollViewport() = placeHolderYPositionOnScreen > screenHeight

    private fun isPlaceHolderAboveScrollViewport() = placeHolderYPositionOnScreen < 0
}