package com.example.mapleleaf.ui.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller


class TopLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context,
        orientation,
        reverseLayout) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int,
    ) {
        val smoothScroller: SmoothScroller = TopSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private class TopSmoothScroller internal constructor(context: Context?) :
        LinearSmoothScroller(context) {
        /**
         * 以下参数以LinearSmoothScroller解释
         * @param viewStart RecyclerView的top位置
         * @param viewEnd RecyclerView的bottom位置
         * @param boxStart Item的top位置
         * @param boxEnd Item的bottom位置
         * @param snapPreference 判断滑动方向的标识（The edge which the view should snap to when entering the visible
         * area. One of [.SNAP_TO_START], [.SNAP_TO_END] or
         * [.SNAP_TO_END].）
         * @return 移动偏移量
         */
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int,
        ): Int {
            return boxStart - viewStart // 这里是关键，得到的就是置顶的偏移量
        }
    }
}


































