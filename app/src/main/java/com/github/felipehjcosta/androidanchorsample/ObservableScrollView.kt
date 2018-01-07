package com.github.felipehjcosta.androidanchorsample

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class ObservableScrollView : ScrollView {

    private val callbacks by lazy { mutableListOf<ObservableScrollViewCallbacks>() }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        callbacks.forEach { it.onScrollChanged(t - oldt) }


//        val metrics = resources.displayMetrics
//        val screenWidth = metrics.widthPixels.toFloat()
//        val screenHeight = metrics.heightPixels.toFloat()
//
//        val documentHeight = (computeVerticalScrollRange() * screenHeight) / computeVerticalScrollExtent()
//
//        val topY = (documentHeight * computeVerticalScrollOffset()) / computeVerticalScrollRange()

        android.util.Log.e("ObservableScrollView", ">>>>> topY: ${topY}")
    }

    val topY: Int
        get() {
            val metrics = resources.displayMetrics
            val screenHeight = metrics.heightPixels

            val documentHeight = (computeVerticalScrollRange() * screenHeight) / computeVerticalScrollExtent()

            return (documentHeight * computeVerticalScrollOffset()) / computeVerticalScrollRange()
        }

    fun addScrollViewCallbacks(callbacks: ObservableScrollViewCallbacks) = this.callbacks.add(callbacks)

    fun removeScrollViewCallbacks(callbacks: ObservableScrollViewCallbacks) = this.callbacks.remove(callbacks)

    interface ObservableScrollViewCallbacks {
        fun onScrollChanged(deltaY: Int)
    }
}