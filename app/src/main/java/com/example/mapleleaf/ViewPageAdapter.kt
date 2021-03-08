package com.example.mapleleaf


import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.viewpager.widget.PagerAdapter
import java.util.*


/**
 * 功能： ViewPageAdapt
 */
class ViewPageAdapter @JvmOverloads constructor(
    private var mViews: List<View>,
    private var mTitles: List<String>
) : PagerAdapter() {

    @Nullable
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(mViews[position])
        return mViews[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mViews[position])
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return if (mTitles!!.isNotEmpty()) mTitles!![position] else ""
    }

    override fun getCount(): Int {
        return mViews.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }


    init {
        if (mTitles == null) {
            mTitles = ArrayList()
        }
    }
}
