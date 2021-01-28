package com.mikewellback.redditgallery.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class SuspendableViewPager(context: Context?, attrs: AttributeSet?): ViewPager(context!!, attrs) {

    var swipeable = true

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return try {
            swipeable && super.onInterceptTouchEvent(ev)
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
            false
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            false
        }
    }
}