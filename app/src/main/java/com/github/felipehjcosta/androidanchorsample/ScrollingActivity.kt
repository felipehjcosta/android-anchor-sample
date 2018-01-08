package com.github.felipehjcosta.androidanchorsample

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver


class ScrollingActivity : AppCompatActivity() {

    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val observableScrollView by bindView<ObservableScrollView>(R.id.observable_scroll_view)

    private val placeHolder by bindView<View>(R.id.place_holder)
    private val anchor by bindView<View>(R.id.anchor)

    private lateinit var anchorBehaviorStrategy: AnchorBehaviorStrategy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scrolling_activity)
        setSupportActionBar(toolbar)

        anchorBehaviorStrategy = AnchorBehaviorStrategy(anchor, placeHolder)
        observableScrollView.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    addScrollViewCallbacks(anchorBehaviorStrategy)

                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    anchorBehaviorStrategy.onScrollChanged(this@run, 0)
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        observableScrollView.run { removeScrollViewCallbacks(anchorBehaviorStrategy) }
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
