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

        callbacks.forEach { it.onScrollChanged(this, t - oldt) }
    }
    fun addScrollViewCallbacks(callbacks: ObservableScrollViewCallbacks) = this.callbacks.add(callbacks)

    fun removeScrollViewCallbacks(callbacks: ObservableScrollViewCallbacks) = this.callbacks.remove(callbacks)

    interface ObservableScrollViewCallbacks {
        fun onScrollChanged(observableScrollView: ObservableScrollView, deltaY: Int)
    }
}