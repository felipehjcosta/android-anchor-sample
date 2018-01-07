package com.github.felipehjcosta.androidanchorsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.scrolling_activity.*


class ScrollingActivity : AppCompatActivity() {

    private val callback = object : ObservableScrollView.ObservableScrollViewCallbacks {
        override fun onScrollChanged(deltaY: Int) {
            val scrollView = findViewById<ObservableScrollView>(R.id.observable_scroll_view)
            val scrollY = scrollView.scrollY
            val toolbar = findViewById<View>(R.id.toolbar)

            val placeHolder = findViewById<View>(R.id.place_holder)

            val location = intArrayOf(0, 0)
            placeHolder.getLocationOnScreen(location)


            val dm = DisplayMetrics().apply { windowManager.defaultDisplay.getMetrics(this) }
            val topContainer = findViewById<View>(R.id.top_container)
            val topOffset = (dm.heightPixels - topContainer.measuredHeight) + toolbar.height
            android.util.Log.e("SCROLLING", ">>>>> deltaY: ${deltaY} " +
                    "\nscrollY: ${scrollY} " +
                    "\nheight: ${scrollView.height} " +
                    "\nlocation[1]: ${location[1]}" +
                    "\ntopOffset: ${topOffset}" +
                    "\nplace holder top:  ${placeHolder.top} " +
                    "\nplace holder height:  ${placeHolder.height} " +
                    "\nplace holder bottom:  ${placeHolder.bottom} "
            )

            if (location[1] > dm.heightPixels) {
                // OUT TOP
                android.util.Log.e("SCROLLING", ">>>>> OUT TOP")
                anchor.translationY = ((anchor.parent as ViewGroup).height - anchor.height).toFloat()
            } else if (location[1] < 0) {
                // OUT BOTTOM
                android.util.Log.e("SCROLLING", ">>>>> OUT BOTTOM")
                anchor.translationY = (location[1] - topOffset).toFloat()
            } else {
                // SHOW

                val topLocation = dm.heightPixels
                val bottomLocation = dm.heightPixels - anchor.height
                var delta = 0
                if (location[1] > bottomLocation) {
                    delta = location[1] - bottomLocation
                }
                android.util.Log.e("SCROLLING", ">>>>> SHOW")
                anchor.translationY = (location[1] - topOffset - delta).toFloat()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scrolling_activity)
        setSupportActionBar(toolbar)

        findViewById<ObservableScrollView>(R.id.observable_scroll_view)?.run {
            addScrollViewCallbacks(callback)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        findViewById<ObservableScrollView>(R.id.observable_scroll_view)?.run {
            removeScrollViewCallbacks(callback)
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
}
