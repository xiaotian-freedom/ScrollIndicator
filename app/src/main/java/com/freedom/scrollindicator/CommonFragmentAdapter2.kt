package com.freedom.scrollindicator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Copyright © 2019. All rights reserved.<br>
 *
 * @author: TST
 *
 * @date: 2019-09-08
 *
 * @Description:通用fragment适配器
 */
class CommonFragmentAdapter2(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentList: MutableList<Fragment>
) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}