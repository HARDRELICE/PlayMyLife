package com.hardrelice.playmylife.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * OverScrollLayout
 * @author hardrelice
 * @date 21-4-09
 */
class OverScrollLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {
    private var childView: RecyclerView? = null
    private val original = Rect()
    private var isMoved = false
    private var startYpos = 0f
    private var isSuccess = false
    private var mScrollListener: ScrollListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        childView = getChildAt(0) as RecyclerView
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        original[childView!!.left, childView!!.top, childView!!.right] = childView!!.bottom
    }

    fun setScrollListener(listener: ScrollListener?) {
        mScrollListener = listener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val touchYpos = ev.y
        if (touchYpos >= original.bottom || touchYpos <= original.top) {
            if (isMoved) {
                recoverLayout()
            }
            return true
        }
        return when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startYpos = ev.y
                val scrollYpos = (ev.y - startYpos).toInt()
                val pullDown = scrollYpos > 0 && canPullDown()
                val pullUp = scrollYpos < 0 && canPullUp()
                if (pullDown || pullUp) {
                    cancelChild(ev)
                    val offset =
                        (scrollYpos * DAMPING_COEFFICIENT).toInt()
                    childView!!.layout(
                        original.left,
                        original.top + offset,
                        original.right,
                        original.bottom + offset
                    )
                    mScrollListener?.onScroll()
                    isMoved = true
                    isSuccess = false
                    true
                } else {
                    startYpos = ev.y
                    isMoved = false
                    isSuccess = true
                    super.dispatchTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val scrollYpos = (ev.y - startYpos).toInt()
                val pullDown = scrollYpos > 0 && canPullDown()
                val pullUp = scrollYpos < 0 && canPullUp()
                if (pullDown || pullUp) {
                    cancelChild(ev)
                    val offset =
                        (scrollYpos * DAMPING_COEFFICIENT).toInt()
                    childView!!.layout(
                        original.left,
                        original.top + offset,
                        original.right,
                        original.bottom + offset
                    )
                    mScrollListener?.onScroll()
                    isMoved = true
                    isSuccess = false
                    true
                } else {
                    startYpos = ev.y
                    isMoved = false
                    isSuccess = true
                    super.dispatchTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isMoved) {
                    recoverLayout()
                }
                !isSuccess || super.dispatchTouchEvent(ev)
            }
            else -> true
        }
    }

    /**
     * ?????????view?????????????????????
     * @param ev event
     */
    private fun cancelChild(ev: MotionEvent) {
        ev.action = MotionEvent.ACTION_CANCEL
        super.dispatchTouchEvent(ev)
    }

    /**
     * ????????????
     */
    private fun recoverLayout() {
        val anim = TranslateAnimation(
            0F, 0F,
            (childView!!.top - original.top).toFloat(), 0F
        )
        anim.duration = ANIM_TIME.toLong()
        childView!!.startAnimation(anim)
        childView!!.layout(original.left, original.top, original.right, original.bottom)
        isMoved = false
    }

    /**
     * ????????????????????????
     * @return true????????????false:?????????
     */
    private fun canPullDown(): Boolean {
        val firstVisiblePosition =
            (childView!!.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        if (firstVisiblePosition != 0 && childView!!.adapter!!.itemCount != 0) {
            return false
        }
        val mostTop = if (childView!!.childCount > 0) childView!!.getChildAt(0).top else 0
        return mostTop >= 0
    }

    /**
     * ????????????????????????
     * @return true????????????false:?????????
     */
    private fun canPullUp(): Boolean {
        val lastItemPosition = childView!!.adapter!!.itemCount - 1
        val lastVisiblePosition =
            (childView!!.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
        if (lastVisiblePosition >= lastItemPosition) {
            val childIndex =
                lastVisiblePosition - (childView!!.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
            val childCount = childView!!.childCount
            val index = Math.min(childIndex, childCount - 1)
            val lastVisibleChild = childView!!.getChildAt(index)
            if (lastVisibleChild != null) {
                return lastVisibleChild.bottom <= childView!!.bottom - childView!!.top
            }
        }
        return false
    }

    interface ScrollListener {
        /**
         * ??????????????????
         */
        fun onScroll()
    }

    companion object {
        private const val ANIM_TIME = 300
        private const val DAMPING_COEFFICIENT = 0.5f
    }
}