package com.example.mapleleaf

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 * 功能：viewpager 添加　fragments 的适配器
 */
class FragmentAdapter(fragments: List<Fragment>, fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {

    private val mFragments = fragments

    override fun getItem(i: Int): Fragment {
        return mFragments[i]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

}