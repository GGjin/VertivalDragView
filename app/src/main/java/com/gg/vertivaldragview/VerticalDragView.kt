package com.gg.vertivaldragview

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.widget.ListViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView


/**
 * Creator : GG
 * Date    : 2018/2/5
 * Mail    : gg.jin.yu@gmai.com
 * Explain :
 */
class VerticalDragView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 1.创建使头部的View以及下面的View能够自由滑动
     * 2.然后设置条件，使头部的View不动，下面的View只能够上下滑动
     * 3.设置下面的View下滑的最大距离是头部View的高度
     * 4.设置自动展开距离，超过一半展开，小于一半收起
     * 4.1 发现yvel是垂直方向上的手指离开屏幕时候的速度，感觉用这个更好 当速度大于阀值的时候展开
     * 5.重写事件分发，兼容可以滑动的View example：ListView,RecyclerView例子
     */


    private val mDragHelper: ViewDragHelper by lazy { ViewDragHelper.create(this, mDragHelperCallback) }

    private lateinit var mDragView: View

    private var mMenuHeight: Int = 0

    private var mMenuIsOpen = false

    private val mDragHelperCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean = child == mDragView

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int = when {
            top <= 0 -> 0
            top >= mMenuHeight -> mMenuHeight
            else -> top
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            Log.w("top", "=======" + mDragView.top + "------" + mMenuHeight)
            if (mDragView == releasedChild) {
                if (yvel <= 600) {
                    mDragHelper.settleCapturedViewAt(0, 0)
                    mMenuIsOpen = false
                } else {
                    mDragHelper.settleCapturedViewAt(0, mMenuHeight)
                    mMenuIsOpen = true
                }
                invalidate()
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            mDragHelper.processTouchEvent(event)
        }
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (childCount != 2) {
            throw RuntimeException("VerticalDragView must has two childView")
        }

        mMenuHeight = getChildAt(0).measuredHeight
        mDragView = getChildAt(1)
    }

    override fun computeScroll() {
        if (mDragHelper.continueSettling(true))
            invalidate()
    }

    private var mDownY = 0
    private var mMoveY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //菜单打开的时候 就需要拦截
        if (mMenuIsOpen)
            return true

        Log.w("down", "-------" + mDownY)
        Log.w("move", "--------" + mMoveY)
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownY = ev.y.toInt()
                mDragHelper.processTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                mMoveY = ev.y
                if ((mMoveY - mDownY) > 0 && !canChildScrollUp()) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }


    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    private fun canChildScrollUp(): Boolean = if (mDragView is ListView) {
        ListViewCompat.canScrollList(mDragView as ListView, -1)
    } else mDragView.canScrollVertically(-1)


}