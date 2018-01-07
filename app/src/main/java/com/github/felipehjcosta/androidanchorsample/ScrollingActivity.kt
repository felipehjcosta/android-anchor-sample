package com.github.felipehjcosta.androidanchorsample

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.*
import kotlinx.android.synthetic.main.scrolling_activity.*


class ScrollingActivity : AppCompatActivity(), ObservableScrollView.ObservableScrollViewCallbacks {

    private val toolbar by bindView<Toolbar>(R.id.toolbar)

    private val observableScrollView by bindView<ObservableScrollView>(R.id.observable_scroll_view)

    private val placeHolder by bindView<View>(R.id.place_holder)
    private val topContainer by bindView<View>(R.id.top_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scrolling_activity)
        setSupportActionBar(toolbar)

        observableScrollView.run {
            addScrollViewCallbacks(this@ScrollingActivity)

            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    onScrollChanged(0)
                }
            })
        }
    }

    override fun onScrollChanged(deltaY: Int) {

        val placeHolderYPositionOnScreen = intArrayOf(0, 0).apply { placeHolder.getLocationOnScreen(this) }[1]

        val screenHeight = screenHeight()
        val topOffset = calculateScrollViewTop(screenHeight)

        anchor.translationY = if (isScreenBelow(placeHolderYPositionOnScreen, screenHeight)) {
            ((anchor.parent as ViewGroup).height - anchor.height).toFloat()
        } else if (isScreenAbove(placeHolderYPositionOnScreen)) {
            (placeHolderYPositionOnScreen - topOffset).toFloat()
        } else {
            val bottomLocation = screenHeight - anchor.height
            val delta = if (placeHolderYPositionOnScreen > bottomLocation) {
                placeHolderYPositionOnScreen - bottomLocation
            } else {
                0
            }
            (placeHolderYPositionOnScreen - topOffset - delta).toFloat()
        }
    }

    private fun screenHeight(): Int = with(DisplayMetrics()) {
        windowManager.defaultDisplay.getMetrics(this)
        heightPixels
    }

    private fun calculateScrollViewTop(screenHeight: Int) = (screenHeight - topContainer.measuredHeight) + toolbar.height

    private fun isScreenBelow(placeHolderYPositionOnScreen: Int, screenHeight: Int) = placeHolderYPositionOnScreen > screenHeight

    private fun isScreenAbove(placeHolderYPositionOnScreen: Int) = placeHolderYPositionOnScreen < 0

    override fun onDestroy() {
        super.onDestroy()
        observableScrollView.run {
            removeScrollViewCallbacks(this@ScrollingActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings ->
            true
        else -> super.onOptionsItemSelected(item)
    }

    inline fun <reified T : View> Activity.bindView(@IdRes idRes: Int): Lazy<T> {
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) as T }
    }
}
